package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // use real db for test, not use db in memory
@Rollback(false)  // keep the test result in db
public class CategoryRepositoryTests {
	
	@Autowired
	private CategoryRepository repo;
	
	@Test
	public void testCreateRootCategory() { // test creating top-level(root-level) category
		Category category = new Category("Cell Phones");
		Category savedCategory = repo.save(category);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSubCategory() { // test creating sub categories
		Category parent = new Category(7);
		Category subCategory = new Category("Cell Phones", parent);
		Category savedCategory = repo.save(subCategory);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testGetCategory() {  // test getting a category and its children
		Category category = repo.findById(1).get();
		System.out.println(category.getName() + " subCategory: ");
		
		Set<Category> children = category.getChildren();
		for(Category c : children) {
			System.out.println(c.getName());
		}
		
		assertThat(children.size()).isGreaterThan(0);
	}
	
	@Test
	public void testGetHierarchicalCategories() {  // test printing categories in hierarchical form
		Iterable<Category> categories = repo.findAll();
		
		for(Category c: categories) {
			if(c.getParent() == null) {
				System.out.println(c.getName());
				
				Set<Category> children = c.getChildren();
				
				for(Category subCategory : children) {
					System.out.println("--" + subCategory.getName());
					getChildrenCategories(subCategory, 1);
				}
			}
		}
	}

	public void getChildrenCategories(Category parent, int subLevel) { // test printing sub-level(children) categories in a category
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			for (int i = 0; i < newSubLevel; i++) {
				System.out.println("--");
			}
			System.out.println("--" + subCategory.getName());

			getChildrenCategories(subCategory, newSubLevel);
		}
	}

	
	@Test
	public void testListRootCategories() { // test getting all the root-categories
		List<Category> roootCategories = repo.findRootCategories();
		roootCategories.forEach(cat -> System.out.println(cat.getName()));
	}
	
	@Test
	public void testFindByName() {
		String name = "Computers";
		Category category = repo.findByName(name);
		
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}
	
}
