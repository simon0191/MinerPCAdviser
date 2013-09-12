/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "PRODUCT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findByProductId", query = "SELECT p FROM Product p WHERE p.productPK.productId = :productId"),
    @NamedQuery(name = "Product.findByModel", query = "SELECT p FROM Product p WHERE p.model = :model"),
    @NamedQuery(name = "Product.findByBrandId", query = "SELECT p FROM Product p WHERE p.productPK.brandId = :brandId")})
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductPK productPK;
    @Basic(optional = false)
    @Column(name = "MODEL")
    private String model;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductAddition> productAdditionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
    private List<MpcaComment> mpcaCommentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductWebPage> productWebPageList;
    @JoinColumn(name = "BRAND_ID", referencedColumnName = "BRAND_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Brand brand;

    public Product() {
    }

    public Product(ProductPK productPK) {
        this.productPK = productPK;
    }

    public Product(ProductPK productPK, String model) {
        this.productPK = productPK;
        this.model = model;
    }

    public Product(BigInteger productId, BigInteger brandId) {
        this.productPK = new ProductPK(productId, brandId);
    }

    public ProductPK getProductPK() {
        return productPK;
    }

    public void setProductPK(ProductPK productPK) {
        this.productPK = productPK;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @XmlTransient
    public List<ProductAddition> getProductAdditionList() {
        return productAdditionList;
    }

    public void setProductAdditionList(List<ProductAddition> productAdditionList) {
        this.productAdditionList = productAdditionList;
    }

    @XmlTransient
    public List<MpcaComment> getMpcaCommentList() {
        return mpcaCommentList;
    }

    public void setMpcaCommentList(List<MpcaComment> mpcaCommentList) {
        this.mpcaCommentList = mpcaCommentList;
    }

    @XmlTransient
    public List<ProductWebPage> getProductWebPageList() {
        return productWebPageList;
    }

    public void setProductWebPageList(List<ProductWebPage> productWebPageList) {
        this.productWebPageList = productWebPageList;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productPK != null ? productPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.productPK == null && other.productPK != null) || (this.productPK != null && !this.productPK.equals(other.productPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Product[ productPK=" + productPK + " ]";
    }
    
}
