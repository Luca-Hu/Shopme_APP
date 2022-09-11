package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shopme.common.Constants;

@Entity
@Table(name="categories")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true, length = 128, nullable = false)
	private String name;
	
	@Column(length = 128, nullable = false)
	private String image;
	
	@Column(name = "all_parent_ids", length = 256, nullable = true)
	private String allParentIDs;
	
	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();
	
	public Category() {
	}
	
	public Category(Integer id) {
		this.id = id;
	}
	
	public Category(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static Category copyIdAndName(Category cat) { // create constructor method: copy (only id and name) , for the "category_form.html"
		Category category  = new Category();
		category.setId(cat.getId());
		category.setName(cat.getName());
		return category;
	}

	public static Category copyIdAndName(Integer id, String name) { // Note: this constructor's arguments are different.
		Category category  = new Category();
		category.setId(id);
		category.setName(name);
		return category;
	}
	
	public static Category copyFull(Category cat) {// create constructor method: copy, for the "categories.html"
		Category category  = new Category();
		category.setId(cat.getId());
		category.setName(cat.getName());
		category.setImage(cat.getImage());
		category.setHasChildren(category.getChildren().size() > 0);
		
		return category;
	}
	
	public static Category copyFull(Category cat, String name) {// copy a category object and give a new name
		Category category  = Category.copyFull(cat);
		category.setName(name);
		return category;
	}
	
	public Category(String name) {
		this.name = name;
		this.image = "default.png";
	}
	
	public Category(String name, Category parent) {
		this(name); // call the first constructor
		this.parent = parent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	@Transient
	public String getImagePath() { // send the local images file to client page
		if(this.id == null) {
			return "/images/image-thumbnail.png";
		}
		return Constants.S3_BASE_URI + "/category-images/" + this.id + "/" + this.image;
	}
	
	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@Transient
	private boolean hasChildren;
	
	@Override
	public String toString() {
		return this.name;
	}

	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}
	

}
