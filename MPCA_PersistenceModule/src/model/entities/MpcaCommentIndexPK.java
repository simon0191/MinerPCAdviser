/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Antonio
 */
@Embeddable
public class MpcaCommentIndexPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "MPCA_INDEX_TYPE_INDEX_ID")
    private long mpcaIndexTypeIndexId;
    @Basic(optional = false)
    @Column(name = "MPCA_COMMENT_COMMENT_ID")
    private long mpcaCommentCommentId;

    public MpcaCommentIndexPK() {
    }

    public MpcaCommentIndexPK(long mpcaIndexTypeIndexId, long mpcaCommentCommentId) {
        this.mpcaIndexTypeIndexId = mpcaIndexTypeIndexId;
        this.mpcaCommentCommentId = mpcaCommentCommentId;
    }

    public long getMpcaIndexTypeIndexId() {
        return mpcaIndexTypeIndexId;
    }

    public void setMpcaIndexTypeIndexId(long mpcaIndexTypeIndexId) {
        this.mpcaIndexTypeIndexId = mpcaIndexTypeIndexId;
    }

    public long getMpcaCommentCommentId() {
        return mpcaCommentCommentId;
    }

    public void setMpcaCommentCommentId(long mpcaCommentCommentId) {
        this.mpcaCommentCommentId = mpcaCommentCommentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) mpcaIndexTypeIndexId;
        hash += (int) mpcaCommentCommentId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaCommentIndexPK)) {
            return false;
        }
        MpcaCommentIndexPK other = (MpcaCommentIndexPK) object;
        if (this.mpcaIndexTypeIndexId != other.mpcaIndexTypeIndexId) {
            return false;
        }
        if (this.mpcaCommentCommentId != other.mpcaCommentCommentId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaCommentIndexPK[ mpcaIndexTypeIndexId=" + mpcaIndexTypeIndexId + ", mpcaCommentCommentId=" + mpcaCommentCommentId + " ]";
    }
    
}
