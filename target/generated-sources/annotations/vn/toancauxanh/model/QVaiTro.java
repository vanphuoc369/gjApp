package vn.toancauxanh.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVaiTro is a Querydsl query type for VaiTro
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVaiTro extends EntityPathBase<VaiTro> {

    private static final long serialVersionUID = 1036975434L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QVaiTro vaiTro = new QVaiTro("vaiTro");

    public final QModel _super;

    public final StringPath alias = createString("alias");

    public final BooleanPath checkKichHoat = createBoolean("checkKichHoat");

    //inherited
    public final BooleanPath daXoa;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final QNhanVien nguoiSua;

    // inherited
    public final QNhanVien nguoiTao;

    public final SetPath<String, StringPath> quyens = this.<String, StringPath>createSet("quyens", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> soThuTu = createNumber("soThuTu", Integer.class);

    public final StringPath tenVaiTro = createString("tenVaiTro");

    //inherited
    public final StringPath trangThai;

    public QVaiTro(String variable) {
        this(VaiTro.class, forVariable(variable), INITS);
    }

    public QVaiTro(Path<? extends VaiTro> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVaiTro(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVaiTro(PathMetadata metadata, PathInits inits) {
        this(VaiTro.class, metadata, inits);
    }

    public QVaiTro(Class<? extends VaiTro> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
    }

}

