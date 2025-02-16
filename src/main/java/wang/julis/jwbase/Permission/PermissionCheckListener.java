package wang.julis.jwbase.Permission;


public interface PermissionCheckListener {

    void startPermissionCheck(PermissionListener target, String... permission);

}
