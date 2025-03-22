# Personalized Data API

This Spring Boot application provides a service that offers personalized product recommendations to shoppers.

## Features

- Ingest product metadata and shopper personalized product lists
- Retrieve personalized product recommendations with filtering options
- High-performance API for eCommerce integration

## Technical Stack

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL

### Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE niq_personalized_data;
```

Configure your database credentials in `application.properties`.

### Building and Running

1. Clone the repository
2. Navigate to the project directory
3. Build the project:

```bash
mvn clean install
```

4. Run the application:

```bash
mvn spring-boot:run
```

## API Documentation

### Internal API (for data ingestion)

#### Save Product Metadata
```
POST /api/internal/product-metadata
Content-Type: application/json

{
  "productId": "BB-2144746855",
  "category": "Babies",
  "brand": "Babyom"
}
```

#### Save Shopper Shelf
```
POST /api/internal/shopper-shelf
Content-Type: application/json

{
  "shopperId": "S-1000",
  "shelf": [
    {
      "productId": "MB-2093193398",
      "relevancyScore": 31.089209569320897
    },
    {
      "productId": "BB-2144746855",
      "relevancyScore": 55.16626010671777
    }
  ]
}
```

### External API (for eCommerce)

#### Get Personalized Products
```
GET /api/external/products?shopperId=S-1000&category=Babies&brand=Babyom&limit=10
```

Parameters:
- `shopperId` (required): The ID of the shopper
- `category` (optional): Filter by product category
- `brand` (optional): Filter by product brand
- `limit` (optional, default=10, max=100): Maximum number of products to return

## Performance Considerations

1. **Database Indexes**: The application uses proper indexes on the `shopper_id` and `product_id` fields to ensure fast queries.

2. **Pagination**: The API supports limiting the number of results, which helps with performance when dealing with large datasets.

3. **Caching**: To further improve performance, consider adding caching (e.g., Redis) for frequently accessed shopper data.

4. **Database Optimization**: The database schema is designed to optimize read operations for the external API.

5. **Connection Pooling**: Spring Boot's built-in connection pooling helps manage database connections efficiently.

## Database Design

The application uses the following database tables:

1. `products` - Stores product metadata
2. `shopper` - Stores shopper information
3. `shopper_products` - Stores the personalized product lists for each shopper



To use this Dockerfile:
First, build your Spring Boot application:
Build the Docker image:
Run the container:
This will create and run a container with your Spring Boot application, mapping port 8080 from the container to port 8080 on your host machine.