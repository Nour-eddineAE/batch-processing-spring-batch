package com.example.tp5traitementbash.batch.readers;

import com.example.tp5traitementbash.entities.dtos.TransactionDTO;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("XMLTransactionReader")
public class TransactionXMLReader extends SynchronizedItemStreamReader<TransactionDTO> {
    Resource inputFileResource;

    public TransactionXMLReader(
            @Qualifier("transactionsXMLResource") Resource inputFileResource
    ) {
        this.inputFileResource = inputFileResource;
        this.setDelegate(xmlReader());
    }

    private XStreamMarshaller getXStearmMarshaller() {
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("transaction", TransactionDTO.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliases);

        // ! only allow parsing TransactionDTO-like elements
        marshaller.setSupportedClasses(TransactionDTO.class);

        // ! defining converters
        marshaller.setConverters(new CustomXMLTransactionConverter());

        // ! security permissions
        marshaller.getXStream().addPermission(NoTypePermission.NONE);
        marshaller.getXStream().addPermission(NullPermission.NULL);
        marshaller.getXStream().addPermission(PrimitiveTypePermission.PRIMITIVES);
        marshaller.getXStream().allowTypes(new Class[] {TransactionDTO.class});


        return marshaller;
    }

    private StaxEventItemReader<TransactionDTO> xmlReader() {
        var xmlReader = new StaxEventItemReader<TransactionDTO>();
        xmlReader.setResource(inputFileResource);
        xmlReader.setFragmentRootElementName("transaction");
        xmlReader.setUnmarshaller(getXStearmMarshaller());

        return xmlReader;
    }

    private static class CustomXMLTransactionConverter implements Converter {

        @Override
        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            TransactionDTO transactionDTO = new TransactionDTO();

            transactionDTO.setTransactionId(Long.parseLong(reader.getAttribute("idTransaction")));

            while (reader.hasMoreChildren()) {
                reader.moveDown();
                String nodeName = reader.getNodeName();

                if ("montant".equals(nodeName)) {
                    transactionDTO.setAmount(Double.parseDouble(reader.getValue()));
                } else if ("dateTransaction".equals(nodeName)) {
                    try {
                        String dateStr = reader.getValue();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = dateFormat.parse(dateStr);
                        transactionDTO.setTransactionDate(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if ("idCompte".equals(nodeName)) {
                    transactionDTO.setAccountId(Long.parseLong(reader.getValue()));
                }

                reader.moveUp();
            }

            return transactionDTO;
        }

        @Override
        public boolean canConvert(Class type) {
            return type.equals(TransactionDTO.class);
        }
    }
}
