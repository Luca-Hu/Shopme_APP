package com.shopme.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shope.common.exception.CategoryNotFoundException;
import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	
	@Autowired 
	private CategoryRepository repo;
	
	public List<Category> listNoChildrenCategories(){
		
		List<Category> listNoChildrenCategories = new ArrayList<>();
		
		List<Category> listAllCategories = repo.findAll();
		
		listAllCategories.forEach(category -> {
			Set<Category> children = category.getChildren();
			if(children == null || children.size() == 0) {
				listNoChildrenCategories.add(category);
			}
		});
		
		return listNoChildrenCategories;
	}
	
	public Category getCategory(String name) throws CategoryNotFoundException {
		Category category = repo.findByName(name);
		if(category == null) {
			throw new CategoryNotFoundException("Could not find any categories with name: " + name);
		}
		return repo.findByName(name);
	}
	
	public List<Category> getCategoryParents(Category child){
		List<Category> listParents = new ArrayList<>();
		
		Category parent = child.getParent();
		
		while(parent != null) { // find parents nodes until reach root node : print the breadcrumb trail
			listParents.add(0, parent);
			parent = parent.getParent();
		}
		
		listParents.add(child);
		
		return listParents;
	}
}
