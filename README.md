# Enoca Spring-Boot E-Commerce Application 

This README file provides an overview of the Enoca Spring-Boot E-Commerce Application

## Project Overview ##

This Spring Boot project aims to develop an e-commerce system with entities such as Product, Customer, Cart, and Order.

## Features ##

- Customer and Cart Relationship: Each customer can have one cart, and a cart can contain multiple orders.
- Dynamic Total Price Calculation: The total price of the cart and orders is dynamically calculated and updated with each operation (addition, removal, quantity change) within the cart.
- Historical Price Tracking: After placing an order, customers can view the historical prices of the products they purchased, even if the prices have been updated later.
- Stock Management: Stock tracking is implemented to prevent placing orders for products that are out of stock.

## Implemented Services ##

- Customer Operations: AddCustomer
- Product Operations: GetProduct, CreateProduct, UpdateProduct, DeleteProduct
- Cart Operations: GetCart, UpdateCart, EmptyCart, AddProductToCart, RemoveProductFromCart
- Order Operations: PlaceOrder, GetOrderForCode, GetAllOrdersForCustomer
- 
## Database Configuration

### Database URL
The EnocaDB application connects to a Microsoft SQL Server database using the following JDBC URL:

spring.datasource.url=jdbc:sqlserver://localhost;databaseName=enocadb;encrypt=true;trustServerCertificate=true

- `jdbc:sqlserver://localhost`: Specifies the database server hostname (localhost in this case).
- `databaseName=enocadb`: Specifies the name of the database (enocadb in this case).
- `encrypt=true`: Enables encryption for the connection.
- `trustServerCertificate=true`: Instructs the client to trust the server's certificate without validating it.

### Hibernate DDL Auto
The EnocaDB application uses Hibernate to manage database schema changes. The `spring.jpa.hibernate.ddl-auto` property is set to `update`, which means that Hibernate will automatically update the database schema based on the entity mappings when the application starts.

## API Documentation Configuration

### API Docs Path
The EnocaDB application uses Springdoc OpenAPI to generate API documentation. The `springdoc.api-docs.path` property is set to `/api-docs`, which specifies the endpoint path for accessing the generated OpenAPI documentation.

