# Spring Batch Project Readme

## Project Overview

This Spring Batch project is designed to process and manage transactions data. It features customized readers for both CSV and XML files,
a decorator for logging the reading process, transaction processors for debiting and mapping, and a writer for handling the output.

### Project Structure

-   **/batch/readers**: Contains customized readers for CSV and XML files, along with a decorator for logging the reading process.

-   **/batch/processors**: Houses transaction processors, including one for debiting and another for mapping the input from readers.

-   **/batch/writers**: Contains the writer responsible for handling the output.

-   **/batch/BatchConfig**: Provides the basic configuration for the Spring Batch to process the files.

-   **/dtos/TransactionDTO**: Holds the exposed data of a transaction.

-   **/services**: Includes an abstraction of AccountServices and TransactionServices, along with implementations for each.

-   **/entities**: Contains the basic schemas used in the project, namely Account and Transaction.

-   **/start**: Serves for setting up the data and configuring the batch launching.

    -   **/start/DataSetup**: Generates 24 demo accounts for populating the database.

    -   **/start/BatchLauncher**: Configures a batch and runs it.

-   **/resources**: Input files are available as components throughout the configuration component.

-   **/logs**: Logs of the app can be found here, including ERROR level logs and INFO level logs.

-   **CommandLineRunner**: Injects both DataSetup and BatchLauncher components, allowing for their execution when the application runs.

## things i learned

-   Creating a simple Spring Batch project, focusing on key features such as state management, design patterns, and highly custom batch components.
