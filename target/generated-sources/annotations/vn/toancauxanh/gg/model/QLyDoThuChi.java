package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLyDoThuChi is a Querydsl query type for LyDoThuChi
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLyDoThuChi extends EntityPathBase<LyDoThuChi> {

    private static final long serialVersionUID = 249473916L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QLyDoThuChi lyDoThuChi = new QLyDoThuChi("lyDoThuChi");

    public final vn.toancauxanh.model.QModel _super;

    public final BooleanPath completed = createBoolean("completed");

    public final StringPath content = createString("content");

    //inherited
    public final BooleanPath daXoa;

    //inherited
    public final NumberPath<Long> id;

    public final NumberPath<Integer> mucThu = createNumber("mucThu", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final BooleanPath thu = createBoolean("thu");

    //inherited
    public final StringPath trangThai;

    public final StringPath trangThaiThu = createString("trangThaiThu");

    public QLyDoThuChi(String variable) {
        this(LyDoThuChi.class, forVariable(variable), INITS);
    }

    public QLyDoThuChi(Path<? extends LyDoThuChi> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLyDoThuChi(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLyDoThuChi(PathMetadata metadata, PathInits inits) {
        this(LyDoThuChi.class, metadata, inits);
    }

    public QLyDoThuChi(Class<? extends LyDoThuChi> type, PathMetadata metadata, PathInits inits) {
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

