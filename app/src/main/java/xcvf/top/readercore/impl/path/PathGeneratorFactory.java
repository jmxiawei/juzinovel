package xcvf.top.readercore.impl.path;

import xcvf.top.readercore.interfaces.IPathGenerator;

public class PathGeneratorFactory {

    static IPathGenerator pathGenerator;
    private static final IPathGenerator defaultGenerator = new MD5PathGenerator();

    public static void setPathGenerator(IPathGenerator pathGenerator) {
        PathGeneratorFactory.pathGenerator = pathGenerator;
    }
    public static IPathGenerator get(){
        if(pathGenerator == null){
            pathGenerator = defaultGenerator;
        }
        return pathGenerator;
    }

}
