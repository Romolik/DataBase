package main.java.ru.nsu.shchiptsov;

import java.sql.Date;

public class DatabaseFields {
	private String idBuyer;
	private String nameProduct;
	private String firstName;
	private String lastName;
	private Double moneySpent;
	private String idEmployee;
	private String idPost;
	private Double salary;
	private String numberPhone;
	private String idInvoice;
	private Date date;
	private String idCompositionOfInvoice;
	private Integer idPointOfSale;
	private Integer idProduct;
	private String idSupplier;
	private Double cost;
	private String idTypePointOfSale;
	private Integer numberSections;
	private Integer numberHalls;
	private Integer numberStalls;
	private Double size;
	private Double rentalPrice;
	private Double utilities;
	private String namePost;
	private String idTypeProduct;
	private String articleNumber;
	private Integer numberOfPieces;
	private Double quantity;
	private String idPurchase;
	private String nameSupplier;
	private String nameTypePointOfSale;
	private String nameTypeProduct;
	private String lastIdBuyer;
	private String lastIdEmployee;
	private String lastIdInvoice;
	private String lastIdOrderShop;
	private Double costPiece;
	private Double cost100g;

	public Double getTradeTurnover() {
		return tradeTurnover;
	}

	public void setTradeTurnover(Double tradeTurnover) {
		this.tradeTurnover = tradeTurnover;
	}

	private Double tradeTurnover;

	public boolean isNewRow() {
		return newRow;
	}

	public void setNewRow(boolean newRow) {
		this.newRow = newRow;
	}

	private boolean newRow = false;

	public String getLastIdBuyer() {
		return lastIdBuyer;
	}

	public void setLastIdBuyer(String lastIdBuyer) {
		this.lastIdBuyer = lastIdBuyer;
	}


	public Double getCostPiece() {
		return costPiece;
	}

	public void setCostPiece(Double costPiece) {
		this.costPiece = costPiece;
	}

	public Double getCost100g() {
		return cost100g;
	}

	public void setCost100g(Double cost100g) {
		this.cost100g = cost100g;
	}

	public String getNameProduct() {
		return nameProduct;
	}

	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}


	public String getLastIdEmployee() {
		return lastIdEmployee;
	}

	public void setLastIdEmployee(String lastIdEmployee) {
		this.lastIdEmployee = lastIdEmployee;
	}

	public String getLastIdInvoice() {
		return lastIdInvoice;
	}

	public void setLastIdInvoice(String lastIdInvoice) {
		this.lastIdInvoice = lastIdInvoice;
	}

	public String getLastIdOrderShop() {
		return lastIdOrderShop;
	}

	public void setLastIdOrderShop(String lastIdOrderShop) {
		this.lastIdOrderShop = lastIdOrderShop;
	}

	public String getLastIdPointSale() {
		return lastIdPointSale;
	}

	public void setLastIdPointSale(String lastIdPointSale) {
		this.lastIdPointSale = lastIdPointSale;
	}

	public String getLastIdPost() {
		return lastIdPost;
	}

	public void setLastIdPost(String lastIdPost) {
		this.lastIdPost = lastIdPost;
	}

	public String getLastIdPurchase() {
		return lastIdPurchase;
	}

	public void setLastIdPurchase(String lastIdPurchase) {
		this.lastIdPurchase = lastIdPurchase;
	}

	public String getLastIdSupplier() {
		return lastIdSupplier;
	}

	public void setLastIdSupplier(String lastIdSupplier) {
		this.lastIdSupplier = lastIdSupplier;
	}

	public String getLastIdTypePointSale() {
		return lastIdTypePointSale;
	}

	public void setLastIdTypePointSale(String lastIdTypePointSale) {
		this.lastIdTypePointSale = lastIdTypePointSale;
	}

	public String getLastIdTypeProduct() {
		return lastIdTypeProduct;
	}

	public void setLastIdTypeProduct(String lastIdTypeProduct) {
		this.lastIdTypeProduct = lastIdTypeProduct;
	}

	private String lastIdPointSale;
	private String lastIdPost;
	private String lastIdPurchase;
	private String lastIdSupplier;
	private String lastIdTypePointSale;
	private String lastIdTypeProduct;


	public DatabaseFields() {

	}
	public String getIdEmployee() {
		return idEmployee;
	}

	public void setIdEmployee(String idEmployee) {
		this.idEmployee = idEmployee;
	}

	public String getIdPost() {
		return idPost;
	}

	public void setIdPost(String idPost) {
		this.idPost = idPost;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public String getNumberPhone() {
		return numberPhone;
	}

	public void setNumberPhone(String numberPhone) {
		this.numberPhone = numberPhone;
	}

	public String getIdInvoice() {
		return idInvoice;
	}

	public void setIdInvoice(String idInvoice) {
		this.idInvoice = idInvoice;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getIdCompositionOfInvoice() {
		return idCompositionOfInvoice;
	}

	public void setIdCompositionOfInvoice(String idCompositionOfInvoice) {
		this.idCompositionOfInvoice = idCompositionOfInvoice;
	}

	public Integer getIdPointOfSale() {
		return idPointOfSale;
	}

	public void setIdPointOfSale(Integer idPointOfSale) {
		this.idPointOfSale = idPointOfSale;
	}

	public Integer getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Integer idProduct) {
		this.idProduct = idProduct;
	}

	public String getIdSupplier() {
		return idSupplier;
	}

	public void setIdSupplier(String idSupplier) {
		this.idSupplier = idSupplier;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getIdTypePointOfSale() {
		return idTypePointOfSale;
	}

	public void setIdTypePointOfSale(String idTypePointOfSale) {
		this.idTypePointOfSale = idTypePointOfSale;
	}

	public Integer getNumberSections() {
		return numberSections;
	}

	public void setNumberSections(Integer numberSections) {
		this.numberSections = numberSections;
	}

	public Integer getNumberHalls() {
		return numberHalls;
	}

	public void setNumberHalls(Integer numberHalls) {
		this.numberHalls = numberHalls;
	}

	public Integer getNumberStalls() {
		return numberStalls;
	}

	public void setNumberStalls(Integer numberStalls) {
		this.numberStalls = numberStalls;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public Double getRentalPrice() {
		return rentalPrice;
	}

	public void setRentalPrice(Double rentalPrice) {
		this.rentalPrice = rentalPrice;
	}

	public Double getUtilities() {
		return utilities;
	}

	public void setUtilities(Double utilities) {
		this.utilities = utilities;
	}

	public String getNamePost() {
		return namePost;
	}

	public void setNamePost(String namePost) {
		this.namePost = namePost;
	}

	public String getIdTypeProduct() {
		return idTypeProduct;
	}

	public void setIdTypeProduct(String idTypeProduct) {
		this.idTypeProduct = idTypeProduct;
	}

	public String getArticleNumber() {
		return articleNumber;
	}

	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}

	public Integer getNumberOfPieces() {
		return numberOfPieces;
	}

	public void setNumberOfPieces(Integer numberOfPieces) {
		this.numberOfPieces = numberOfPieces;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getIdPurchase() {
		return idPurchase;
	}

	public void setIdPurchase(String idPurchase) {
		this.idPurchase = idPurchase;
	}

	public String getNameSupplier() {
		return nameSupplier;
	}

	public void setNameSupplier(String nameSupplier) {
		this.nameSupplier = nameSupplier;
	}

	public String getNameTypePointOfSale() {
		return nameTypePointOfSale;
	}

	public void setNameTypePointOfSale(String nameTypePointOfSale) {
		this.nameTypePointOfSale = nameTypePointOfSale;
	}

	public String getNameTypeProduct() {
		return nameTypeProduct;
	}

	public void setNameTypeProduct(String nameTypeProduct) {
		this.nameTypeProduct = nameTypeProduct;
	}

	public String getIdBuyer() {
		return idBuyer;
	}

	public void setIdBuyer(String id_Buyer) {
		this.idBuyer = id_Buyer;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Double getMoneySpent() {
		return moneySpent;
	}

	public void setMoneySpent(Double moneySpent) {
		this.moneySpent = moneySpent;
	}


}
