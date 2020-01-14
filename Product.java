import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

enum ProductCategory{
	Electronics, Book, Home, Bicycles
}

public class Product {
	public static ArrayList<Product> AllProducts = new ArrayList<Product>();
	public static ArrayList<Product> BlackFridayProducts = new ArrayList<Product>();
	
	public Map<String, String> properties = new HashMap<>();
	public ArrayList<ProductCategory> categories = new ArrayList<ProductCategory>();
	
	public Product(Map<String, String> properties, ArrayList<ProductCategory> categories){
		this.properties = properties;
		this.categories = categories;
		
		AllProducts.add(this);
	}
	
	public synchronized void setQuantity(int quantity) throws NotEnoughQuantityException, NumberFormatException {
		if(Integer.parseInt(properties.get("quantity")) < quantity) {
			throw new NotEnoughQuantityException("Not enough stock!");
		}else {
			Integer newQ = Integer.parseInt(properties.get("quantity")) - quantity;
			properties.put("quantity", newQ.toString());
		}
	}
	
	public void setPromoPrice(double discount) throws PromoPriceException, NumberFormatException {
		if(discount <= 0 || discount >= 100) {
			throw new PromoPriceException("Discount value is invalid!");
		}
		Double currentPrice = Double.parseDouble(properties.get("price"));
		Double promoPrice = currentPrice - currentPrice*discount/100.0;
		if(promoPrice < Double.parseDouble(properties.get("minPrice"))) {
			throw new PromoPriceException("New price cannot be below the minimal price!");
		}else {
			properties.put("promoPrice", promoPrice.toString());
		}
	}
	
	public void setMinPrice(double minPrice) throws NumberFormatException {
		Double minP = minPrice;
		
		if(minPrice > Double.parseDouble(properties.get("price"))) {
			properties.put("minPrice", minP.toString());
			properties.put("price", minP.toString());
		} else {
			properties.put("minPrice", minP.toString());
		}
	}

	public void setPrice(double newPrice) throws PromoPriceException, NumberFormatException {
		if(newPrice < Double.parseDouble(properties.get("minPrice"))) {
			throw new PromoPriceException("New price cannot be below the minimal price!");
		}else {
			Double newP = newPrice;
			properties.put("price", newP.toString());
		}
	}
	
	public void addProperty(String key, String value) {
		properties.put(key, value);
	}
	
	public void addCategory(ProductCategory cat) {
		categories.add(cat);
	}

	public void removeProperty(String key, String value) {
		properties.remove(key, value);	
	}
	
	public void removeCategory(ProductCategory cat) {
		categories.remove(cat);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public ArrayList<ProductCategory> getCategories() {
		return categories;
	}

	@Override
	public String toString() {
		int spacing = 2;
		StringBuilder line = new StringBuilder();
		line.append(properties.get("manufacturer") + "  ");
		line.append(properties.get("model") + "\t");
		for(String key : properties.keySet()) {
			if (!key.contentEquals("manufacturer") && !key.contentEquals("model")) {
				if(key.contentEquals("promoPrice")) {
					if(properties.get(key) == null || properties.get(key).contentEquals("0") || properties.get(key).contentEquals("0.0")) {
						line.append(key + ": N/A");
						line.append("  ");
						spacing += 1;
					} else {
						line.append(key + ": " + properties.get(key));
						line.append("  ");
						spacing += 2;
					}
				} else if (spacing<=4) {
					line.append(key + ": " + properties.get(key));
					line.append("  ");
					spacing += 2;
				} else if (spacing >= 6) {
					line.append("\n");
					spacing = 0;
					line.append(key + ": " + properties.get(key));
					line.append("  ");
					spacing += 2;
				}
			}
		}
		return line.toString();
	}
}
