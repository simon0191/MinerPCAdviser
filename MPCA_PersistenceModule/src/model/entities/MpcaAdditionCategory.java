/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "MPCA_ADDITION_CATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaAdditionCategory.findAll", query = "SELECT m FROM MpcaAdditionCategory m"),
    @NamedQuery(name = "MpcaAdditionCategory.findByCategoryId", query = "SELECT m FROM MpcaAdditionCategory m WHERE m.categoryId = :categoryId"),
    @NamedQuery(name = "MpcaAdditionCategory.findByName", query = "SELECT m FROM MpcaAdditionCategory m WHERE m.name = :name")})
public class MpcaAdditionCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CATEGORY_ID")
    @SequenceGenerator(name="SEQ_ADDITION_CATEGORY", sequenceName = "MPCA_ADDITION_CATEGORY_CATEGOR", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ADDITION_CATEGORY")
    private Long categoryId;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @OneToMany(mappedBy = "categoryId", fetch = FetchType.LAZY)
    private List<MpcaAdditionType> mpcaAdditionTypeList;

    public MpcaAdditionCategory() {
    }

    public MpcaAdditionCategory(Long categoryId) {
        this.categoryId = categoryId;
    }

    public MpcaAdditionCategory(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<MpcaAdditionType> getMpcaAdditionTypeList() {
        return mpcaAdditionTypeList;
    }

    public void setMpcaAdditionTypeList(List<MpcaAdditionType> mpcaAdditionTypeList) {
        this.mpcaAdditionTypeList = mpcaAdditionTypeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaAdditionCategory)) {
            return false;
        }
        MpcaAdditionCategory other = (MpcaAdditionCategory) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaAdditionCategory[ categoryId=" + categoryId + " ]";
    }
    
}
