/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
@Table(name = "ADDITION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Addition.findAll", query = "SELECT a FROM Addition a"),
    @NamedQuery(name = "Addition.findByAddId", query = "SELECT a FROM Addition a WHERE a.addId = :addId"),
    @NamedQuery(name = "Addition.findByAddType", query = "SELECT a FROM Addition a WHERE a.addType = :addType")})
public class Addition implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ADD_ID")
    private BigDecimal addId;
    @Basic(optional = false)
    @Column(name = "ADD_TYPE")
    private String addType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addition", fetch = FetchType.LAZY)
    private List<ProductAddition> productAdditionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addition", fetch = FetchType.LAZY)
    private List<CommentAddition> commentAdditionList;

    public Addition() {
    }

    public Addition(BigDecimal addId) {
        this.addId = addId;
    }

    public Addition(BigDecimal addId, String addType) {
        this.addId = addId;
        this.addType = addType;
    }

    public BigDecimal getAddId() {
        return addId;
    }

    public void setAddId(BigDecimal addId) {
        this.addId = addId;
    }

    public String getAddType() {
        return addType;
    }

    public void setAddType(String addType) {
        this.addType = addType;
    }

    @XmlTransient
    public List<ProductAddition> getProductAdditionList() {
        return productAdditionList;
    }

    public void setProductAdditionList(List<ProductAddition> productAdditionList) {
        this.productAdditionList = productAdditionList;
    }

    @XmlTransient
    public List<CommentAddition> getCommentAdditionList() {
        return commentAdditionList;
    }

    public void setCommentAdditionList(List<CommentAddition> commentAdditionList) {
        this.commentAdditionList = commentAdditionList;
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
        if (!(object instanceof Addition)) {
            return false;
        }
        Addition other = (Addition) object;
        if ((this.addId == null && other.addId != null) || (this.addId != null && !this.addId.equals(other.addId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Addition[ addId=" + addId + " ]";
    }
    
}
