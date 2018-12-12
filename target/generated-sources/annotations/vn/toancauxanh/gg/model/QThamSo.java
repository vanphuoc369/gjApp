package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QThamSo is a Querydsl query type for ThamSo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QThamSo extends EntityPathBase<ThamSo> {

    private static final long serialVersionUID = -1180262755L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QThamSo thamSo = new QThamSo("thamSo");

    public final vn.toancauxanh.model.QModel _super;

    //inherited
    public final BooleanPath daXoa;

    //inherited
    public final NumberPath<Long> id;

    public final EnumPath<vn.toancauxanh.gg.model.enums.ThamSoEnum> ma = createEnum("ma", vn.toancauxanh.gg.model.enums.ThamSoEnum.class);

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    //inherited
    public final StringPath trangThai;

    public final StringPath value = createString("value");

    public QThamSo(String variable) {
        this(ThamSo.class, forVariable(variable), INITS);
    }

    public QThamSo(Path<? extends ThamSo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QThamSo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QThamSo(PathMetadata metadata, PathInits inits) {
        this(ThamSo.class, metadata, inits);
    }

    public QThamSo(Class<? extends ThamSo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new vn.toancauxanh.model.QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
    }

}

