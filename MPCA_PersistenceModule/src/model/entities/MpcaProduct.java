/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "MPCA_PRODUCT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaProduct.findAll", query = "SELECT m FROM MpcaProduct m"),
    @NamedQuery(name = "MpcaProduct.findByProductId", query = "SELECT m FROM MpcaProduct m WHERE m.productId = :productId"),
    @NamedQuery(name = "MpcaProduct.findByModel", query = "SELECT m FROM MpcaProduct m WHERE m.model = :model")})
public class MpcaProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PRODUCT_ID")
    private Long productId;
    @Basic(optional = false)
    @Column(name = "MODEL")
    private String model;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mpcaProduct", fetch = FetchType.LAZY)
    private List<MpcaProductAddition> mpcaProductAdditionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId", fetch = FetchType.LAZY)
    private List<MpcaComment> mpcaCommentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mpcaProductProductId", fetch = FetchType.LAZY)
    private List<MpcaProductIndex> mpcaProductIndexList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mpcaProduct", fetch = FetchType.LAZY)
    private List<MpcaProductWebPage> mpcaProductWebPageList;

    public MpcaProduct() {
    }

    public MpcaProduct(Long productId) {
        this.productId = productId;
    }

    public MpcaProduct(Long productId, String model) {
        this.productId = productId;
        this.model = model;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @XmlTransient
    public List<MpcaProductAddition> getMpcaProductAdditionList() {
        return mpcaProductAdditionList;
    }

    public void setMpcaProductAdditionList(List<MpcaProductAddition> mpcaProductAdditionList) {
        this.mpcaProductAdditionList = mpcaProductAdditionList;
    }

    @XmlTransient
    public List<MpcaComment> getMpcaCommentList() {
        return mpcaCommentList;
    }

    public void setMpcaCommentList(List<MpcaComment> mpcaCommentList) {
        this.mpcaCommentList = mpcaCommentList;
    }

    @XmlTransient
    public List<MpcaProductIndex> getMpcaProductIndexList() {
        return mpcaProductIndexList;
    }

    public void setMpcaProductIndexList(List<MpcaProductIndex> mpcaProductIndexList) {
        this.mpcaProductIndexList = mpcaProductIndexList;
    }

    @XmlTransient
    public List<MpcaProductWebPage> getMpcaProductWebPageList() {
        return mpcaProductWebPageList;
    }

    public void setMpcaProductWebPageList(List<MpcaProductWebPage> mpcaProductWebPageList) {
        this.mpcaProductWebPageList = mpcaProductWebPageList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaProduct)) {
            return false;
        }
        MpcaProduct other = (MpcaProduct) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaProduct[ productId=" + productId + " ]";
    }
    
}
