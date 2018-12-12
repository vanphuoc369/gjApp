package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCar is a Querydsl query type for Car
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCar extends EntityPathBase<Car> {

    private static final long serialVersionUID = -42874637L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QCar car = new QCar("car");

    public final vn.toancauxanh.model.QModel _super;

    public final StringPath carUrl = createString("carUrl");

    //inherited
    public final BooleanPath daXoa;

    public final StringPath description = createString("description");

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    //inherited
    public final NumberPath<Long> id;

    public final StringPath medium = createString("medium");

    public final StringPath name = createString("name");

    public final DateTimePath<java.util.Date> ngaySanXuat = createDateTime("ngaySanXuat", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final BooleanPath noiBat = createBoolean("noiBat");

    public final QDonViHanhChinh quan;

    public final StringPath small = createString("small");

    public final QDonViHanhChinh thanhPho;

    //inherited
    public final StringPath trangThai;

    public final NumberPath<Integer> width = createNumber("width", Integer.class);

    public QCar(String variable) {
        this(Car.class, forVariable(variable), INITS);
    }

    public QCar(Path<? extends Car> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCar(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCar(PathMetadata metadata, PathInits inits) {
        this(Car.class, metadata, inits);
    }

    public QCar(Class<? extends Car> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new vn.toancauxanh.model.QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.quan = inits.isInitialized("quan") ? new QDonViHanhChinh(forProperty("quan"), inits.get("quan")) : null;
        this.thanhPho = inits.isInitialized("thanhPho") ? new QDonViHanhChinh(forProperty("thanhPho"), inits.get("thanhPho")) : null;
        this.trangThai = _super.trangThai;
    }

}

