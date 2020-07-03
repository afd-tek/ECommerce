package com.example.bisanat.DAL.Network;

import com.example.bisanat.DAL.Entites.Person;
import com.example.bisanat.DAL.Repository.CategoriesProductDAL;
import com.example.bisanat.DAL.Repository.CategoryDAL;
import com.example.bisanat.DAL.Repository.CommentDAL;
import com.example.bisanat.DAL.Repository.OrderDAL;
import com.example.bisanat.DAL.Repository.OrderLineItemDAL;
import com.example.bisanat.DAL.Repository.PersonDAL;
import com.example.bisanat.DAL.Repository.ProductDAL;

public class HttpServices {
    public static Person _currentUser = null;
    public static CategoriesProductDAL categoriesProductService = HttpClient.getClient().create(CategoriesProductDAL.class);
    public static CategoryDAL categoryService = HttpClient.getClient().create(CategoryDAL.class);
    public static CommentDAL commentService = HttpClient.getClient().create(CommentDAL.class);
    public static OrderDAL orderService = HttpClient.getClient().create(OrderDAL.class);
    public static OrderLineItemDAL orderLineItemService = HttpClient.getClient().create(OrderLineItemDAL.class);
    public static PersonDAL personService = HttpClient.getClient().create(PersonDAL.class);
    public static ProductDAL productService = HttpClient.getClient().create(ProductDAL.class);

}

