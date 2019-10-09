package wang.julis.jwbase.Permission;



public interface PermissionListener {

    void execute();

    void showDenied();

    void showNeverAsk();
}
