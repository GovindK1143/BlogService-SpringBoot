package com.web.blogservice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.web.blogservice.DTO.BlogDTO;
import com.web.blogservice.Dao.BlogDao;
import com.web.blogservice.Model.Blog;
import com.web.blogservice.mapper.BlogMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class BlogService {

	@Autowired
	private BlogDao repo;

	@Autowired
	private EntityManager entityManager; // Entity Manager for creating queries
	
	 @Transactional
	    public ResponseEntity<Long> createBlogPost(Blog blogPost) {
	        try {
	            Long creatorId = blogPost.getCreatorId();

	            // Fetch user data using a native SQL query
	            String sqlQuery = "SELECT id FROM govind_db.user WHERE id = :creatorId";
	            Query query = entityManager.createNativeQuery(sqlQuery);
	            query.setParameter("creatorId", creatorId);

	            Long id = (Long) query.getSingleResult();

	            if (id == null) {
	                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	            }
	            repo.save(blogPost);
	            return new ResponseEntity<>(blogPost.getId(), HttpStatus.CREATED);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @Transactional
	    public ResponseEntity<Blog> getBlogById(Long id) {
	        try {
	            Blog existingBlog = repo.findById(id).orElse(null);
	            if (existingBlog == null) {
	                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	            }
	            return new ResponseEntity<>(existingBlog, HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @Transactional
	    public ResponseEntity<String> editBlogPost(Long blogId, BlogDTO updatedBlogDTO) {
	        try {
	            Blog existingBlog = repo.findById(blogId).orElse(null);
	            if (existingBlog == null) {
	                return new ResponseEntity<>("Blog post not found", HttpStatus.NOT_FOUND);
	            }
	            BlogMapper.BlogDTOToBlog(updatedBlogDTO, existingBlog);
	            repo.save(existingBlog);
	            return new ResponseEntity<>("Blog post updated successfully", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("Failed to update blog post", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @Transactional
	    public ResponseEntity<String> deleteBlog(Long id) {
	        try {
	            Blog existingBlog = repo.findById(id).orElse(null);
	            if (existingBlog == null) {
	                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	            }
	            repo.deleteById(id);
	            return new ResponseEntity<>("Blog Deleted Successfully", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("Failed to delete blog post", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @Transactional
	    public ResponseEntity<List<Blog>> latestBlogs() {
	        try {
	            List<Blog> blogs = repo.findAllByOrderByDateandTimeDesc();
	            if (!blogs.isEmpty()) {
	                return new ResponseEntity<>(blogs, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @Transactional
	    public ResponseEntity<List<Blog>> getBlogsByTags(List<String> tags) {
	        try {
	            List<Blog> blogs = repo.findByTagsInOrderByDateandTimeDesc(tags);
	            if (!blogs.isEmpty()) {
	                return new ResponseEntity<>(blogs, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @Transactional
	    public ResponseEntity<List<Blog>> CreatorBlogs(Long id) {
	        try {
	            List<Blog> blogs = repo.findByCreatorId(id);
	            if (!blogs.isEmpty()) {
	                return new ResponseEntity<>(blogs, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @Transactional
	    public ResponseEntity<List<Blog>> searchBlogs(String query) {
	        try {
	            List<Blog> matchingBlogs = repo.findByTitleContainingOrContentContaining(query, query);
	            if (!matchingBlogs.isEmpty()) {
	                return new ResponseEntity<>(matchingBlogs, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @Transactional
	    public ResponseEntity<String> addComment(Long blogid, String comment) {
	        try {
	            Blog b = repo.findById(blogid).orElse(null);
	            if (b != null) {
	                List<String> comments = b.getComments();
	                comments.add(comment);
	                b.setComments(comments);
	                repo.save(b);
	                return new ResponseEntity<>("Added comment successfully", HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>("No blog exists to add comment", HttpStatus.NO_CONTENT);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("Adding comment failed", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @Transactional
	    public ResponseEntity<List<String>> getComments(Long id) {
	        try {
	            Blog b = repo.findById(id).orElse(null);
	            if (b != null) {
	                List<String> comments = b.getComments();
	                return new ResponseEntity<>(comments, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}
