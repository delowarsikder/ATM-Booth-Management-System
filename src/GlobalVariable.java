public class GlobalVariable {
    int winX, winY;
    int windowWidth, windowHeight;
    static String assetsPath;
    /*File file = new File("assets");
    String absolutePath = file.getAbsolutePath();*/

    public GlobalVariable() {
        this.winX = 400;
        this.winY = 150;
        this.windowWidth = 370;
        this.windowHeight = 400;
        assetsPath = "E:\\Java\\ATM_BOOTH\\assets";
    }

    public static void main(String[] args) {
        GlobalVariable globalVariable = new GlobalVariable();
        System.out.println("winX: " + globalVariable.winX);
        System.out.println("winY: " + globalVariable.getWinY());
        System.out.println("WindowWidth: " + globalVariable.getWindowWidth());
        System.out.println("windowHeight: " + globalVariable.getWindowHeight());
    }

    public int getWinX() {
        return winX;
    }

    public int getWinY() {
        return winY;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public String getAssetsPath() {
        return assetsPath;
    }


}
