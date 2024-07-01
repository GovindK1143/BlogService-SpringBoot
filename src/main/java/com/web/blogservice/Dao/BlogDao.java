package com.web.blogservice.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.blogservice.Model.Blog;

public interface BlogDao extends JpaRepository<Blog, Long> {
	
	public List<Blog> findAllByOrderByDateandTimeDesc();
	public List<Blog> findByTagsInOrderByDateandTimeDesc(List<String> tags);
	public List<Blog> findByCreatorId(Long id);
	public List<Blog> findByTitleContainingOrContentContaining(String title, String content);
//	public List<Blog> findByTagsAndTitle(List<String> tags,String title);
	
}
