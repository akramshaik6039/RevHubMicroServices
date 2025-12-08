# Post Service Entity Alignment Fixes

## Overview
Fixed entity table structure in post-service to match the monolith (RevHub) database schema, ensuring proper database compatibility and functionality.

## Issues Fixed

### 1. Comment Entity
**Problems:**
- Missing `createdDate` field with `@CreationTimestamp`
- Missing Lombok constructors (`@NoArgsConstructor`, `@AllArgsConstructor`)
- Column names not explicitly defined (userId should map to `author_id`, postId to `post_id`)

**Fixed:**
- Added `@CreationTimestamp` on `createdDate` field
- Added `@NoArgsConstructor` and `@AllArgsConstructor` annotations
- Explicitly defined column names: `author_id`, `post_id`, `parent_comment_id`

### 2. Like Entity
**Problems:**
- Missing `createdDate` field with `@CreationTimestamp`
- Missing Lombok constructors
- Column names not explicitly defined

**Fixed:**
- Added `@CreationTimestamp` on `createdDate` field
- Added `@NoArgsConstructor` and `@AllArgsConstructor` annotations
- Explicitly defined column names: `user_id`, `post_id`

### 3. Share Entity
**Problems:**
- Missing `createdDate` field with `@CreationTimestamp`
- Missing Lombok constructors
- Column names not explicitly defined

**Fixed:**
- Added `@CreationTimestamp` on `createdDate` field
- Added `@NoArgsConstructor` and `@AllArgsConstructor` annotations
- Explicitly defined column names: `user_id`, `post_id`

### 4. Post Entity
**Problems:**
- Missing Lombok constructors

**Fixed:**
- Added `@NoArgsConstructor` and `@AllArgsConstructor` annotations

### 5. Hashtag Entity
**Problems:**
- Missing Lombok constructors

**Fixed:**
- Added `@NoArgsConstructor` and `@AllArgsConstructor` annotations

### 6. CommentRepository
**Problems:**
- Method names used `CreatedAt` but entity field is `createdDate`

**Fixed:**
- Changed `findByPostIdAndParentCommentIdIsNullOrderByCreatedAtDesc` to `findByPostIdAndParentCommentIdIsNullOrderByCreatedDateDesc`
- Changed `findByParentCommentIdOrderByCreatedAtAsc` to `findByParentCommentIdOrderByCreatedDateAsc`

### 7. CommentResponse DTO
**Problems:**
- Missing `createdDate` field

**Fixed:**
- Added `createdDate` field of type `LocalDateTime`
- Updated `fromComment` method to include `createdDate`

## Database Schema Alignment

### Tables Structure (Matching Monolith)

#### posts
- id (BIGINT, PK, AUTO_INCREMENT)
- content (VARCHAR(1000), NOT NULL)
- image_url (LONGTEXT)
- media_type (VARCHAR(255))
- visibility (VARCHAR(255), NOT NULL, DEFAULT 'PUBLIC')
- author_id (BIGINT, NOT NULL, FK to users.id)
- likes_count (INT, NOT NULL, DEFAULT 0)
- comments_count (INT, NOT NULL, DEFAULT 0)
- shares_count (INT, NOT NULL, DEFAULT 0)
- created_date (DATETIME)

#### comments
- id (BIGINT, PK, AUTO_INCREMENT)
- content (VARCHAR(500), NOT NULL)
- author_id (BIGINT, NOT NULL, FK to users.id)
- post_id (BIGINT, NOT NULL, FK to posts.id)
- parent_comment_id (BIGINT, FK to comments.id)
- created_date (DATETIME)

#### likes
- id (BIGINT, PK, AUTO_INCREMENT)
- user_id (BIGINT, NOT NULL, FK to users.id)
- post_id (BIGINT, NOT NULL, FK to posts.id)
- created_date (DATETIME)
- UNIQUE(user_id, post_id)

#### shares
- id (BIGINT, PK, AUTO_INCREMENT)
- user_id (BIGINT, NOT NULL, FK to users.id)
- post_id (BIGINT, NOT NULL, FK to posts.id)
- created_date (DATETIME)

#### hashtags
- id (BIGINT, PK, AUTO_INCREMENT)
- name (VARCHAR(255), UNIQUE, NOT NULL)
- count (INT, NOT NULL, DEFAULT 1)

## Key Differences: Monolith vs Microservice Approach

### Monolith (RevHub)
- Uses JPA relationships (`@ManyToOne`, `@OneToMany`)
- Direct entity references (e.g., `User author`, `Post post`)
- Cascade operations handled by JPA

### Microservice (Post-Service)
- Uses plain Long IDs for cross-service references
- No JPA relationships for entities in other services
- Manual cascade operations
- Feign clients for cross-service communication

## Testing Checklist

- [ ] Create a new post (with and without media)
- [ ] Retrieve posts (universal and followers feed)
- [ ] Add comments to posts
- [ ] Add replies to comments
- [ ] Like/unlike posts
- [ ] Share posts
- [ ] Delete posts (verify cascade delete of comments, likes, shares)
- [ ] Search posts by content
- [ ] Verify hashtag extraction and counting
- [ ] Check timestamps are properly set on all entities

## Next Steps

1. Restart the post-service
2. Verify database tables are created/updated correctly
3. Test post creation through the API
4. Verify data consistency with existing monolith data
5. Test all CRUD operations
