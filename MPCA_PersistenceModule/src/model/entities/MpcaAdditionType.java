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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "MPCA_ADDITION_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaAdditionType.findAll", query = "SELECT m FROM MpcaAdditionType m"),
    @NamedQuery(name = "MpcaAdditionType.findByAddId", query = "SELECT m FROM MpcaAdditionType m WHERE m.addId = :addId"),
    @NamedQuery(name = "MpcaAdditionType.findByAddType", query = "SELECT m FROM MpcaAdditionType m WHERE m.addType = :addType"),
    @NamedQuery(name = "MpcaAdditionType.findByAddCategory", query = "SELECT m FROM MpcaAdditionType m WHERE m.categoryId.name = :category")
})
public class MpcaAdditionType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ADD_ID")
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MPCA_ADDITION_TYPE_ADD_ID_SEQ")
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MPCA_ADDITION_TYPE_ADD_ID_SEQ")
    @SequenceGenerator(name="SEQ_ADDITION_TYPE", sequenceName = "MPCA_ADDITION_TYPE_ADD_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ADDITION_TYPE")
    private Long addId;
    @Basic(optional = false)
    @Column(name = "ADD_TYPE")
    private String addType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mpcaAdditionType", fetch = FetchType.LAZY)
    private List<MpcaProductAddition> mpcaProductAdditionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mpcaAdditionType", fetch = FetchType.LAZY)
    private List<MpcaCommentAddition> mpcaCommentAdditionList;
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MpcaAdditionCategory categoryId;

    public MpcaAdditionType() {
    }

    public MpcaAdditionType(Long addId) {
        this.addId = addId;
    }

    public MpcaAdditionType(Long addId, String addType) {
        this.addId = addId;
        this.addType = addType;
    }

    public Long getAddId() {
        return addId;
    }

    public void setAddId(Long addId) {
        this.addId = addId;
    }

    public String getAddType() {
        return addType;
    }

    public void setAddType(String addType) {
        this.addType = addType;
    }

    @XmlTransient
    public List<MpcaProductAddition> getMpcaProductAdditionList() {
        return mpcaProductAdditionList;
    }

    public void setMpcaProductAdditionList(List<MpcaProductAddition> mpcaProductAdditionList) {
        this.mpcaProductAdditionList = mpcaProductAdditionList;
    }

    @XmlTransient
    public List<MpcaCommentAddition> getMpcaCommentAdditionList() {
        return mpcaCommentAdditionList;
    }

    public void setMpcaCommentAdditionList(List<MpcaCommentAddition> mpcaCommentAdditionList) {
        this.mpcaCommentAdditionList = mpcaCommentAdditionList;
    }

    public MpcaAdditionCategory getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(MpcaAdditionCategory categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (addId != null ? addId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaAdditionType)) {
            return false;
        }
        MpcaAdditionType other = (MpcaAdditionType) object;
        if ((this.addId == null && other.addId != null) || (this.addId != null && !this.addId.equals(other.addId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaAdditionType[ addId=" + addId + " ]";
    }
    
}
