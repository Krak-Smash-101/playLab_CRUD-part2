package controllers;

import play.api.Environment;
import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import views.html.*;

// Import models
import models.*;


public class HomeController extends Controller {

    //Declare a private form factory
    private FormFactory formFactory;

    //Inject an instance of this into the Controller via the Constructor
    @Inject
    public HomeController(FormFactory f) {
        this.formFactory = f;
    }

    public Result index(String name)
    {
        return ok(index.render("Welcome to the Home page", name));
    }

    public Result about()
    {
        return ok(about.render());
    }

    public Result products() {
        // Get list of all categories in ascending order
        List<Product> productsList = Product.findAll();
        return ok(products.render(productsList));
    }

    public Result addProduct() {
        //Wrap Product class in a FormFactory instance
        Form<Product> addProductForm = formFactory.form(Product.class);

        //Pass the form object
        return ok(addProduct.render(addProductForm));
    }

    public Result addProductSubmit() {
        //Bind the values submitted to form object based on Product
        Form<Product> newProductForm = formFactory.form(Product.class).bindFromRequest();

        //Check for errors
        if(newProductForm.hasErrors()) {
            return badRequest(addProduct.render(newProductForm));
        }

        //Extract the product from the forms object
        Product newProduct = newProductForm.get();

        //save to DB VIA ebean
        newProduct.save();

        //Set success message in tem flash
        flash("success", "Product " + newProduct.getName() + " has been created");

        //Redirect to the admin home
        return redirect(controllers.routes.HomeController.products());
    }

    //Delete Product by ID
    public Result deleteProduct(Long id){

        //Find Prod by ID
        Product.find.ref(id).delete();

        //Add message to flash
        flash("success", "Product has been deleted");

        //Re-direct to Products
        return redirect(routes.HomeController.products());
    }

}
