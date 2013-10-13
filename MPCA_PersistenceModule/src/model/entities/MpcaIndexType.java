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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SimonXPS
 */
@Entity
@Table(name = "MPCA_INDEX_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaIndexType.findAll", query = "SELECT m FROM MpcaIndexType m"),
    @NamedQuery(name = "MpcaIndexType.findByIndexId", query = "SELECT m FROM MpcaIndexType m WHERE m.indexId = :indexId"),
    @NamedQuery(name = "MpcaIndexType.findByIndexName", query = "SELECT m FROM MpcaIndexType m WHERE m.indexName = :indexName")})
public class MpcaIndexType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INDEX_ID")
    private Long indexId;
    @Basic(optional = false)
    @Column(name = "INDEX_NAME")
    private String indexName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mpcaIndexTypeIndexId", fetch = FetchType.LAZY)
    private List<MpcaCommentIndex> mpcaCommentIndexList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mpcaIndexTypeIndexId", fetch = FetchType.LAZY)
    private List<MpcaProductIndex> mpcaProductIndexList;

    public MpcaIndexType() {
    }

    public MpcaIndexType(Long indexId) {
        this.indexId = indexId;
    }

    public MpcaIndexType(Long indexId, String indexName) {
        this.indexId = indexId;
        this.indexName = indexName;
    }

    public Long getIndexId() {
        return indexId;
    }

    public void setIndexId(Long indexId) {
        this.indexId = indexId;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    @XmlTransient
    public List<MpcaCommentIndex> getMpcaCommentIndexList() {
        return mpcaCommentIndexList;
    }

    public void setMpcaCommentIndexList(List<MpcaCommentIndex> mpcaCommentIndexList) {
        this.mpcaCommentIndexList = mpcaCommentIndexList;
    }

    @XmlTransient
    public List<MpcaProductIndex> getMpcaProductIndexList() {
        return mpcaProductIndexList;
    }

    public void setMpcaProductIndexList(List<MpcaProductIndex> mpcaProductIndexList) {
        this.mpcaProductIndexList = mpcaProductIndexList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (indexId != null ? indexId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaIndexType)) {
            return false;
        }
        MpcaIndexType other = (MpcaIndexType) object;
        if ((this.indexId == null && other.indexId != null) || (this.indexId != null && !this.indexId.equals(other.indexId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaIndexType[ indexId=" + indexId + " ]";
    }
    
}
