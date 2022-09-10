package com.shopme.common.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true, length = 255, nullable = false)
	private String name;
	
	@Column(name="description", unique = true, length = 1024, nullable = false)
	private String description;
	
	@Column(name="created_time")
	private Date createdTime;
	
	@Column(name="in_stock")
	private boolean inStock;
	
	private float price;
	
	@Column(name="main_image", nullable = false)
	private String mainImage;
	
	@ManyToOne
	@JoinColumn(name= "category_id") 
	private Category category; // only need reference which directed from products side to category side
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL) // 
	private Set<ProductImage> images = new HashSet<>();
	
	public Product(Integer id) { 
		this.id = id;
	}

	public Product() {
	}
	
	public Product(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public Set<ProductImage> getImages() {
		return images;
	}

	public void setImages(Set<ProductImage> images) {
		this.images = images;
	}
	
	public void addExtraImage(String imageName) {  // add a method to add product-image to images-set
		this.images.add(new ProductImage(imageName, this));
	}
	
	@Transient
	public String getMainImagePath() {
		if(id == null || mainImage == null) {
			return "/images/image-thumbnail.png";
		}
		return "/product-images/" + this.id + "/" + this.mainImage;
	}
	
	@Transient
	public String getShortName() {
		if (name.length() > 30) {
			return name.substring(0, 30).concat("...");
		}
		return name;
	}
	
}
