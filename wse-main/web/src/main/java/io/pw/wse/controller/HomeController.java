package io.pw.wse.controller;
import io.pw.wse.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
	private static final List<Product> PRODUCTS = new ArrayList(List.of(
			new Product("Product1", "1", 8, 45, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"),
			new Product("Product2", "2", 15, 232, "Nisl tincidunt eget nullam non. Quis hendrerit dolor magna eget est lorem ipsum"),
			new Product("Product3", "3", 28, 45, "In hac habitasse platea dictumst quisque sagittis purus."),
			new Product("Product4", "4", 32, 89, "Pulvinar elementum integer enim neque volutpat ac."),
			new Product("Product5", "5", 11, 122, "Nunc pulvinar sapien et ligula ullamcorper malesuada proin")
	));

	public static void updateQuantity (String serialNumber, String action) {
		PRODUCTS.stream()
				.filter(number -> number.getSerialNumber().equals(serialNumber))
				.forEach(number -> {
					if (action.equals("add")) {
						Integer updatedQuantity = number.getQuantity()+1;
						number.setQuantity(updatedQuantity);
					} else if(action.equals("remove")){
						Integer updatedQuantity = number.getQuantity()-1;
						if(updatedQuantity < 0) {
							number.setQuantity(0);
						} else {
							number.setQuantity(updatedQuantity);
						}
					}
				});
	}

	public static Optional<Product> getProduct (String serialNumber) {
		return PRODUCTS.stream()
				.filter(number -> number.getSerialNumber().equals(serialNumber))
				.findAny();
	}
	@RequestMapping(path = "/updateQuantity/{serialNumber}/{action}", method = RequestMethod.POST)
	public String updateQuantity(@PathVariable("serialNumber") String serialNumber, @PathVariable("action") String action, Model model) {
		updateQuantity(serialNumber, action);
		model.addAttribute("products", PRODUCTS);
		model.addAttribute("updateQuantity", new String());
		model.addAttribute("newProduct", new Product());
		return "index";
	}

	@RequestMapping(path = "/")
	public String showProducts(Model model) {
		model.addAttribute("products", PRODUCTS);
		model.addAttribute("updateQuantity", new String());
		model.addAttribute("newProduct", new Product());
		return "index";
	}

	@RequestMapping(value = "/productDetails/{serialNumber}", method = RequestMethod.GET)
	public String showProductDetails(@PathVariable("serialNumber") String serialNumber, Model model) {
		Product product = getProduct(serialNumber).orElseThrow();
		model.addAttribute("product", product);
		return "productDetails";
	}

	@RequestMapping(path = "/addProduct", method = RequestMethod.POST)
	public String addProduct(Product newProduct, Model model) {
		PRODUCTS.add(newProduct);
		model.addAttribute("newProduct", newProduct);
		return "newProduct";
	}
}
