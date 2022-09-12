1、Introduction To Shopme APP


This Web Application's name is Shopme App.

**It is a comprehensive Ecommerce project**, which is very functional and interactive. This application can be divided into admin part and customer part. You can manage complex object-relational models of various entiries in admin application, and experience the process of online shopping in customer application. 

This full-stack app was built by me while I was taking Nam Ha Minh's Spring Boot course. I developed it with **Java Spring Boot**. **It has been deployed on Heroku and you can access it by following the link below.** But I suggest you take a look at the description before you try to enter these links. By the way, the images you upload will be saved permanently in AMAZON S3 CLOUD.

- As administraters of the website:

https://shopme-admin-yihu.herokuapp.com/

- As visitors and customers:

https://shopme-customer-yihu.herokuapp.com/

< Please wait patiently for the reponse of page. >

 -------

2、The Technologies Involved In This Project

I use SpringToolSuite IDE (Eclipse-based) to build this project, and use **Spring Boot** as framework.

Details: 

- FrontEnd View Layer : **JQuery,  Thymeleaf,  Bootstrap,  Spring RESTful Webservices,  etc**
- Server : **Hibernate,  Spring Data JPA,  etc**
- Database : **MySQL**
- Authentication & Authorization :  **Spring Security**
- Unit & Integration Test :  **JUnit,  AssertJ and  Mockito**
- FrontEnd & BackEnd Deployment :  **Heroku,  AMAZON S3 CLOUD**

 ----------

3、Web Application Function Description


Shopme App is a complete shopping website. **You can test both as a administrater and as a customer.**


-As a administrater of the website, you can manage the users(employees)，categories, products, customers and orders.

In the Admin Application, the main functions are as follows：

- 1、Basic CRUD operations support in all forms:

 You can perform CRUD (create, read, Update, DELETE) operations in all these forms. The data of relevant modules such as "products", "categories", "orders", "customers" will be updated in visitor& customer-side, you can enter another link to see the change.

- 2、Sort & Paging :

 These forms have paging function and sort funciton, they work together to show you the list.

You can use the paginations under these forms to page. Also, you can sort the fields in either positive or reverse order by clicking on the different column titles in the froms.

- 3、Filter/Search Function :

 These pages provides filter/search function, where you can input keywords to find the target quickly.

- 4、 Export Excel File Function :

 The Users Form provides the export Excel file function. So you can get an Excel file including an employee list by clicking the Export button in the Users (Staff) page.

- 5、 Hierarchical Categories And Product Classification:

 Category Form contains hierarchical categories. Every products have their own category. Their classification relationship is reflected in the Categories form, as well as in the Products form.

- 6、 Upload Images for Users, Categories and Products

 You can upload or modify images for "Users", "Categories", "Products". The images will be displayed in the table. Besides, the default image will be displayed in the form if you have not added any images for them.

- 7、 Add Extra Images For Each Product :

 The Products Form allows you to add one main-image and many extra-images for each product. All these images will be shown in the page of each product in customer-side.

- 8、 Authentication & Authorizations :

 Web application has different Authentication & Authorizations for diferent user roles (Admin, Salesperson, Shipper). They have different access permissions in this website. Only when you login an Admin account, you have all permissions.

- 9、 Persistence of Image Files :

 Image files are stored in AMAZZON S3 CLOUD, the data will be persistent. So all pictures uploaded by you will also be saved permanently.


// -----------------------------------------------


- As a visitor & customer, you can view products by category or search specific product. But you should sign up and login if you want use the cart/ order / checkout modules.

- 1、Customer Sign up & Sign in

 Every visitor can sign up to become a customer member and use the cart to shop. The customer data will be updated in the "customers" management page of administrater-side application.

- 1、Authentication & Authorizations:

 Web application has different Authentication & Authorizations for visitors and customer members.

- 2、Product Classification By Categories:

 Product items are displayed on the shopping page by categories. 

- 3、Category Breadcrumbs Navigation Bar:

 Shopping page will display category-breadcrumbs synchronously in the upper left corner. You can click the breadcrumbs to navigate quickly.

- 4、 Zoom Images Function In Each Product's page:

 In each product's homepage, you can zoom images by hovering over the thumbnail pictures.

- 5、 Multifunctional Shopping Cart:

 The CART module supports basic CRUD (create, read, Update, DELETE) operations to manage items in your cart. When the number of items update, page will keep displaying the real-time SubTotal calculation result. 

- 6、 Complete Shopping Experience:

 After you place order, the order data will be updated in the "orders" mangement page of administrater-side application. Shopme staff will deliver the product to your address!

 ----------------------------

4、Access to Test


It has been deployed on Heroku so you can access it through these links：

Please wait patiently for the page to open.

- As Administraters Of The Website:

https://shopme-admin-yihu.herokuapp.com/

< Please wait patiently for the reponse of page. >

You have 3 ways to test:

1、You can login as Nar's account(Admin) : Email: nar@test.com ， Password:tester   

2、You can login as William's account(Shipper) : Email: william@test.com ， Password:tester   

3、You can login as Emery's account(Salesperson) : Email: emery@test.com ， Password:tester  

< Tips: All the users/staff and customers' initial password is the same: "tester", so you can log in other roles (Admin, Salesperson, Shipper) in the users/staff form, different roles has different authentications. >
 
// -----------------------------------------------

- As Visitors And Customers:

https://shopme-customer-yihu.herokuapp.com/

You have 3 ways to test:

1、You can view the products without login.

2、You can signup a new customer account and then login (no auto-login).

3、You can login as Beth's account : Email: beth@gmail.com , Password:tester.

--------------------

5、 Demonstration

- As administraters of the website:

- Homepage of Admin account : Nar

![image](https://user-images.githubusercontent.com/69294450/189517732-1aa2e575-148b-42a1-b584-4126c19bd80b.png)


- Users(staff) Manage Page (The form provides all CRUD operations, and file-export, search, sort, paging functions.):

![image](https://user-images.githubusercontent.com/69294450/189471278-81f125b5-c688-4374-9735-02a37f87700d.png)

- Users(staff) Manage Page (Reversely Sort the form by clicking "User ID" column title again):

![image](https://user-images.githubusercontent.com/69294450/189517806-9b2353cf-3801-4fec-95eb-69d9736c9546.png)


- Category Manage Page（Hierarchical categories）:

![image](https://user-images.githubusercontent.com/69294450/189480506-e8dd42b4-f08d-4f4d-ac06-77d417877092.png)


- View detail about products (When you view detail, the data will be only readable.):

![image](https://user-images.githubusercontent.com/69294450/189471377-cb61bce1-a144-48cb-9050-8850433d9988.png)


- Edit the products:

![image](https://user-images.githubusercontent.com/69294450/189471408-6bea257d-bd34-4b7b-bff7-d000e571de16.png)


- Orders Manager Page(After customers place orders, admin will see the orders)

![image](https://user-images.githubusercontent.com/69294450/189480796-909528d2-d7f7-40dd-9b1f-ba606196d736.png)




// -----------------------------------------------
 

- As visitors and customers

- View all categories (This is HomePage of customer-side application, you can click a category to view all products in this category):
![image](https://user-images.githubusercontent.com/69294450/189471438-eafad388-a7f0-4fe5-8460-31a93cc05ac3.png)


- View a product's detail (You can view the SlideShow of the pictures by moving mouse):
![image](https://user-images.githubusercontent.com/69294450/189471499-1ddc43d1-e9f2-44c1-851b-70d114e735ab.png)


- Shopping Cart Page (Visitor have to login to access to this page.):
![image](https://user-images.githubusercontent.com/69294450/189471729-a778c6e4-e9ad-460a-927b-2469c0c57b28.png)

- Check Out Page:

![image](https://user-images.githubusercontent.com/69294450/189517933-a913db96-b116-44c3-be52-90292f633f95.png)


----------------------

5、Contact Me

If you have any questions or advices, please feel free to contact me.

My Linken Account： www.linkedin.com/in/yi-hu-58852321a

My Email： <yihu@smu.edu> 
