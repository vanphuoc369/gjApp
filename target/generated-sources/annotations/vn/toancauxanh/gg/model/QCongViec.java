package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCongViec is a Querydsl query type for CongViec
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCongViec extends EntityPathBase<CongViec> {

    private static final long serialVersionUID = -1934830185L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QCongViec congViec = new QCongViec("congViec");

    public final vn.toancauxanh.model.QModel _super;

    //inherited
    public final BooleanPath daXoa;

    public final QDuAn duAn;

    //inherited
    public final NumberPath<Long> id;

    public final DateTimePath<java.util.Date> ngayGiao = createDateTime("ngayGiao", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    public final vn.toancauxanh.model.QNhanVien nguoiGiao;

    public final vn.toancauxanh.model.QNhanVien nguoiNhan;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final StringPath noiDung = createString("noiDung");

    public final NumberPath<Double> thoiGian = createNumber("thoiGian", Double.class);

    //inherited
    public final StringPath trangThai;

    public QCongViec(String variable) {
        this(CongViec.class, forVariable(variable), INITS);
    }

    public QCongViec(Path<? extends CongViec> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCongViec(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCongViec(PathMetadata metadata, PathInits inits) {
        this(CongViec.class, metadata, inits);
    }

    public QCongViec(Class<? extends CongViec> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new vn.toancauxanh.model.QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.duAn = inits.isInitialized("duAn") ? new QDuAn(forProperty("duAn"), inits.get("duAn")) : null;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiGiao = inits.isInitialized("nguoiGiao") ? new vn.toancauxanh.model.QNhanVien(forProperty("nguoiGiao"), inits.get("nguoiGiao")) : null;
        this.nguoiNhan = inits.isInitialized("nguoiNhan") ? new vn.toancauxanh.model.QNhanVien(forProperty("nguoiNhan"), inits.get("nguoiNhan")) : null;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
    }

}

