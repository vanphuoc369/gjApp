package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChiTietThuChi is a Querydsl query type for ChiTietThuChi
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChiTietThuChi extends EntityPathBase<ChiTietThuChi> {

    private static final long serialVersionUID = 542752138L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QChiTietThuChi chiTietThuChi = new QChiTietThuChi("chiTietThuChi");

    public final vn.toancauxanh.model.QModel _super;

    public final StringPath content = createString("content");

    //inherited
    public final BooleanPath daXoa;

    //inherited
    public final NumberPath<Long> id;

    public final QLyDoThuChi lyDoThuChi;

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final NumberPath<Integer> soTien = createNumber("soTien", Integer.class);

    //inherited
    public final StringPath trangThai;

    public QChiTietThuChi(String variable) {
        this(ChiTietThuChi.class, forVariable(variable), INITS);
    }

    public QChiTietThuChi(Path<? extends ChiTietThuChi> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChiTietThuChi(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChiTietThuChi(PathMetadata metadata, PathInits inits) {
        this(ChiTietThuChi.class, metadata, inits);
    }

    public QChiTietThuChi(Class<? extends ChiTietThuChi> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new vn.toancauxanh.model.QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.lyDoThuChi = inits.isInitialized("lyDoThuChi") ? new QLyDoThuChi(forProperty("lyDoThuChi"), inits.get("lyDoThuChi")) : null;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
    }

}

