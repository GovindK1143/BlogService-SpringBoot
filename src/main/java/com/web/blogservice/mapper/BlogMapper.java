package com.web.blogservice.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.web.blogservice.DTO.BlogDTO;
import com.web.blogservice.Model.Blog;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BlogMapper {
	@AfterMapping
	public static void BlogDTOToBlog(BlogDTO dto,@MappingTarget Blog blog) {
		if(dto.getContent()!=null) {
			blog.setContent(dto.getContent());
		}
		if(dto.getTags()!=null) {
			blog.setTags(dto.getTags());
		}
		if(dto.getTitle()!=null) {
			blog.setTitle(dto.getTitle());
		}
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);
		blog.setDateandTime(formattedDateTime);		
	}	
    
}

