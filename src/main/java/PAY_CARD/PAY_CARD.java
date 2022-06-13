package PAY_CARD;

import netscape.javascript.JSObject;

public class PAY_CARD {
    private String partnerCode;
    private String categoryID;
    private String productID;
    private String productAmount;
    private String customerID;
    private String partnerTransID;
    private String partnerTransDate;
    private String data;
    private String dataSign;

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getPartnerTransID() {
        return partnerTransID;
    }

    public void setPartnerTransID(String partnerTransID) {
        this.partnerTransID = partnerTransID;
    }

    public String getPartnerTransDate() {
        return partnerTransDate;
    }

    public void setPartnerTransDate(String partnerTransDate) {
        this.partnerTransDate = partnerTransDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataSign() {
        return dataSign;
    }

    public void setDataSign(String dataSign) {
        this.dataSign = dataSign;
    }

    public static final class DataObjectBuilder{
        private String partnerCode;
        private String categoryID;
        private String productID;
        private String productAmount;
        private String customerID;
        private String partnerTransID;
        private String partnerTransDate;
        private String data;
        private String dataSign;

        public DataObjectBuilder(){

        }
        public static DataObjectBuilder aDataObject(){
            return new DataObjectBuilder();
        }

        public DataObjectBuilder withPartnerCode(String partnerCode){
            this.partnerCode =partnerCode;
            return this;
        }

        public DataObjectBuilder withCategoryID(String categoryID){
            this.categoryID =categoryID;
            return this;
        }

        public DataObjectBuilder withProductID(String productID){
            this.productID =productID;
            return this;
        }

        public DataObjectBuilder withCustomerID(String customerID){
            this.customerID =customerID;
            return this;
        }

        public DataObjectBuilder withProductAmount(String productAmount){
            this.productAmount =productAmount;
            return this;
        }


        public DataObjectBuilder withPartnerTransID(String partnerTransID){
            this.partnerTransID =partnerTransID;
            return this;
        }

        public DataObjectBuilder withPartnerTransDate(String partnerTransDate){
            this.partnerTransDate =partnerTransDate;
            return this;
        }

        public DataObjectBuilder withData(String data){
            this.data =data;
            return this;
        }
        public DataObjectBuilder withDataSign(String dataSign){
            this.dataSign =dataSign;
            return this;
        }

        public PAY_CARD build(){
            PAY_CARD model = new PAY_CARD();
            model.setPartnerCode(partnerCode);
            model.setCategoryID(categoryID);
            model.setProductID(productID);
            model.setProductAmount(productAmount);
            model.setCustomerID(customerID);
            model.setPartnerTransID(partnerTransID);
            model.setPartnerTransDate(partnerTransDate);
            model.setData(data);
            model.setDataSign(dataSign);
            return model;
        }
    }
}
