package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Category;

@ExtendWith(MockitoExtension.class) // use Mockito to instance the category repo interface, create a fake(mock) object of this type, then use this object as a parameter
@ExtendWith(SpringExtension.class)
public class CategroyServiceTests {

	@MockBean
	private CategoryRepository repo; 
	
	@InjectMocks
	private CategoryService service;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicationName() {
		Integer id = null;
		String name = "Computers";
		
		Category category = new Category(id, name);
		
		Mockito.when(repo.findByName(name)).thenReturn(category);
		
		String res = service.checkUnique(id, name);
		
		assertThat(res).isEqualTo("DuplicatedName");
	}
}
