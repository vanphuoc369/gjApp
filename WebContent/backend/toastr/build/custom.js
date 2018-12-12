toastr.options = {
  	"closeButton": false,
  	"debug": false,
  	"newestOnTop": false,
  	"progressBar": false,
  	"positionClass": "toast-top-center",
  	"preventDuplicates": true,
  	"onclick": null,
  	"showDuration": "300",
  	"hideDuration": "1000",
  	"timeOut": "5000",
  	"extendedTimeOut": "1000",
  	"showEasing": "swing",
  	"hideEasing": "linear",
  	"showMethod": "fadeIn",
  	"hideMethod": "fadeOut"
};
function toastrSuccess(str, title) {
	toastr.success(str, title);
};
function toastrInfo(str, title) {
	toastr.info(str, title);
};
function toastrWarning(str, title) {
	toastr.warning(str, title);
};
function toastrError(str, title) {
	toastr.error(str, title);
};
