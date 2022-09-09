package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shope.common.exception.CategoryNotFoundException;
import com.shopme.common.entity.Category;

@Service
@Transactional
public class CategoryService {
	public static final int ROOT_CATEGORIES_PER_PAGE = 4;
	
	@Autowired
	private CategoryRepository repo;
	
	public List<Category> listAll(String sortDir){
		Sort sort = Sort.by("name");
		if(sortDir.equals("asc")) {
			sort = sort.ascending();
		}else if(sortDir.equals("desc")) {
			sort = sort.descending();
		}
		
		List<Category> rootCategories = repo.findRootCategories(sort);
		return listHierarchicalCategories(rootCategories, sortDir);
	}
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) { // list all categories(inlude image) in "categories.html"
		List<Category> hierarchicalCategories = new ArrayList<>();
		
		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));	// copy category include image
			// we will reset categories'name(add "--" prefix), this change is persistent.
			// In order to avoid the update in db, we need copy new category objects.
			
			Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);
			
			for (Category subCategory : children) {
				String name = "-->" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name)); // copy a category and give a new name
				
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
			}
		}
		
		return hierarchicalCategories;
	}
	
	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
			Category parent, int subLevel, String sortDir) {  
		Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
		int newSubLevel = subLevel + 1;
		
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {				
				name += "-->";
			}
			name += subCategory.getName();
		
			hierarchicalCategories.add(Category.copyFull(subCategory, name)); // copy a category and give a new name
			
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
		}
		
	}
	
	public Category save(Category category) { // provide method for Controller-saveCategory
		Category parent = category.getParent();
		if (parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) + "-";
			category.setAllParentIDs(allParentIds);
		}
		return repo.save(category);	
	}
	
	
	public List<Category> listCategoriesUsedInForm() { // list Categories used in form (display in "category_form.html")
		List<Category> categoriesUsedInForm = new ArrayList<>();
		
		Iterable<Category> categoriesInDB = repo.findRootCategories(Sort.by("name").ascending());
		
		for (Category category : categoriesInDB) {
			categoriesUsedInForm.add(Category.copyIdAndName(category)); // copyIdAndName: Infact, it is a constructor method: create a new category
			
			Set<Category> children = sortSubCategories(category.getChildren());
			
			for (Category subCategory : children) {
				String name = "-->" + subCategory.getName();
				categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name)); // Note: this "CopyIdAndName" is different constructor method which need different params.
				
				listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
			}
		}		
		
		return categoriesUsedInForm; 
	}
	
	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, 
			Category parent, int subLevel) {  // list all sub-hierarchical categories (children categories)
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());
		
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {				
				name += "-->";
			}
			name += subCategory.getName();
			
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			
			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}		
	}	

	public Category get(Integer id) throws CategoryNotFoundException{
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category with Id: " + id);
		}
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		
		Category categoryFoundByName = repo.findByName(name);
		
		if (isCreatingNew && categoryFoundByName != null) {
			return "DuplicatedName";
		} else if (categoryFoundByName != null && categoryFoundByName.getId() != id) {
			return "DuplicatedName";	
		}
		return "OK";
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category> children) {  // default sort method
		return sortSubCategories(children, "asc");
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) { // sort method: sortDir = "asc" / "desc"
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
			@Override
			public int compare(Category cat1, Category cat2) {
				if (sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});
		 
		sortedChildren.addAll(children);
		
		return sortedChildren;
	}
	
	public void delete(Integer id) throws CategoryNotFoundException { // delete category by id
		Long countById = repo.countById(id);  // just get category id , not a full category object
		if(countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any category with ID:" + id);
		}
		repo.deleteById(id);
	}	
}