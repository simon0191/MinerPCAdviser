/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "PRODUCT_ADDITION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductAddition.findAll", query = "SELECT p FROM ProductAddition p"),
    @NamedQuery(name = "ProductAddition.findByProductProductId", query = "SELECT p FROM ProductAddition p WHERE p.productAdditionPK.productProductId = :productProductId"),
    @NamedQuery(name = "ProductAddition.findByProductBrandId", query = "SELECT p FROM ProductAddition p WHERE p.productAdditionPK.productBrandId = :productBrandId"),
    @NamedQuery(name = "ProductAddition.findByAdditionAddId", query = "SELECT p FROM ProductAddition p WHERE p.productAdditionPK.additionAddId = :additionAddId")})
public class ProductAddition implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductAdditionPK productAdditionPK;
    @Basic(optional = false)
    @Lob
    @Column(name = "VALUE")
    private String value;
    @JoinColumns({
        @JoinColumn(name = "PRODUCT_PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = false, updatable = false),
        @JoinColumn(name = "PRODUCT_BRAND_ID", referencedColumnName = "BRAND_ID", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product product;
    @JoinColumn(name = "ADDITION_ADD_ID", referencedColumnName = "ADD_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Addition addition;

    public ProductAddition() {
    }

    public ProductAddition(ProductAdditionPK productAdditionPK) {
        this.productAdditionPK = productAdditionPK;
    }

    public ProductAddition(ProductAdditionPK productAdditionPK, String value) {
        this.productAdditionPK = productAdditionPK;
        this.value = value;
    }

    public ProductAddition(BigInteger productProductId, BigInteger productBrandId, BigInteger additionAddId) {
        this.productAdditionPK = new ProductAdditionPK(productProductId, productBrandId, additionAddId);
    }

    public ProductAdditionPK getProductAdditionPK() {
        return productAdditionPK;
    }

    public void setProductAdditionPK(ProductAdditionPK productAdditionPK) {
        this.productAdditionPK = productAdditionPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Addition getAddition() {
        return addition;
    }

    public void setAddition(Addition addition) {
        this.addition = addition;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productAdditionPK != null ? productAdditionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductAddition)) {
            return false;
        }
        ProductAddition other = (ProductAddition) object;
        if ((this.productAdditionPK == null && other.productAdditionPK != null) || (this.productAdditionPK != null && !this.productAdditionPK.equals(other.productAdditionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProductAddition[ productAdditionPK=" + productAdditionPK + " ]";
    }
    
}
