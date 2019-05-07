package com.fanglin.utils;

import com.fanglin.core.others.ValidateException;
import com.fanglin.properties.CommonProperties;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * 其他工具类方法
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:37
 **/
@Component
@Slf4j
public class OthersUtils {
    private static CommonProperties commonProperties;
    private static ExecutorService executorService;

    public OthersUtils(CommonProperties commonProperties, ExecutorService executorService) {
        OthersUtils.commonProperties = commonProperties;
        OthersUtils.executorService = executorService;
    }

    /**
     * 向html文件中写入内容
     */
    public static boolean writeHtml(String fileName, String desc, String htmlStyle) {
        try {
            String basePath = getFileSaveParentPath();
            // 模板路径
            String filePath = basePath + "/" + fileName;
            File pathFile = new File(filePath.substring(0, filePath.lastIndexOf("/")));
            if (!pathFile.exists()) {
                boolean success = pathFile.mkdirs();
                if (!success) {
                    log.warn("创建目录失败:{}", pathFile.getName());
                    throw new ValidateException("创建目录失败");
                }
            }
            File file = new File(filePath);
            if (!file.exists()) {
                boolean success = file.createNewFile();
                if (!success) {
                    log.warn("创建文件失败 目录:{} 文件名:{}", file.getPath(), file.getName());
                    throw new ValidateException("创建文件失败");
                }
            }
            // 建立文件输出流
            FileOutputStream fileoutputstream = new FileOutputStream(filePath);
            OutputStreamWriter writer = new OutputStreamWriter(fileoutputstream, StandardCharsets.UTF_8);
            String style;
            if (htmlStyle == null) {
                style = desc;
            } else {
                style = htmlStyle;
                int start = desc.indexOf("<content>");
                int end = desc.indexOf("</content>");
                if (start > 0 && end > 0) {
                    style = style.replace("<content>", desc.substring(start + 9, end));
                } else {
                    style = style.replace("<content>", desc);
                }
            }
            byte[] tagBytes = style.getBytes();
            fileoutputstream.write(tagBytes);
            writer.flush();
            fileoutputstream.close();
            writer.close();
            return true;
        } catch (Exception e) {
            log.warn("html内容写入异常:{}", e.getMessage());
            throw new ValidateException("html内容写入异常");
        }
    }

    /**
     * 读取html内容
     */
    public static String readHtml(String fileName) {
        try {
            String basePath = getFileSaveParentPath();
            // 本地html文件路径
            String filePath = basePath + "/" + fileName;
            String templateContent;
            FileInputStream fileinputstream = new FileInputStream(filePath);
            int length = fileinputstream.available();
            byte[] bytes = new byte[length];
            int readSize = fileinputstream.read(bytes);
            if (readSize <= 0) {
                return "";
            }
            fileinputstream.close();
            templateContent = new String(bytes);
            return templateContent;
        } catch (Exception e) {
            log.warn("读取html内容异常:{}", e.getMessage());
            return "";
        }
    }

    /**
     * 未文件压缩，生成缩略图
     *
     * @param files 文件数组
     * @param small 是否生成缩略图
     * @param path  保存根目录
     * @return
     */
    public static Set<String> uploadFiles(MultipartFile[] files, Boolean small, String path) {
        //图片相对路径数组
        Set<String> fileNames = new HashSet<>();
        //判断file数组不能为空并且长度大于0
        if (files != null && files.length > 0) {
            //生成文件保存相对路径
            path = setDefaultPath(path);
            path += "/" + TimeUtils.getCurrentTime("yyyyMMdd") + "/";
            //循环获取file数组中得文件
            for (MultipartFile multipartFile : files) {
                if (!multipartFile.isEmpty()) {
                    //文件名
                    String fileName = String.valueOf(UUIDUtils.nextId());
                    //文件后缀
                    String suffix = multipartFile.getOriginalFilename().substring(Objects.requireNonNull(multipartFile.getOriginalFilename()).lastIndexOf(".")).toLowerCase();
                    //父目录
                    String basePath = getFileSaveParentPath();
                    //创建目录
                    File file = new File(basePath + path);
                    if (!file.exists()) {
                        boolean success = file.mkdirs();
                        if (!success) {
                            log.warn("创建文件或目录失败:目录{} 文件名{}", file.getPath(), file.getName());
                            throw new ValidateException("创建文件或目录失败");
                        }
                    }
                    String originalFileName = basePath + path + fileName + suffix;
                    //判断是否是可压缩图片
                    boolean isImage = ".jpg".equals(suffix) || ".jpeg".equals(suffix) || ".png".equals(suffix);
                    fileNames.add(path + fileName + suffix);
                    File originalFile = new File(originalFileName);
                    try {
                        multipartFile.transferTo(originalFile);
                        //是图片的才生成缩略图和压缩
                        if (isImage) {
                            String finalPath = path;
                            //从线程池获取线程去异步执行图片压缩任务
                            executorService.execute(() -> {
                                try {
                                    //大于100k的图片才压缩
                                    if (originalFile.length() / 1024 > 100) {
                                        BufferedImage bufferedImage = ImageIO.read(originalFile);
                                        //根据图片宽度进行压缩，高度根据宽度进行等比例缩放
                                        int width = bufferedImage.getWidth() > 1000 ? 1000 : bufferedImage.getWidth();
                                        int height = bufferedImage.getWidth() > 1000 ? (int) (bufferedImage.getHeight() * ((float) width / bufferedImage.getWidth())) : bufferedImage.getHeight();
                                        Thumbnails.of(originalFile)
                                            .size(width, height)
                                            .outputQuality(0.25f)
                                            .toFile(originalFileName);
                                    }
                                    //生成缩略图
                                    if (small != null && small) {
                                        Thumbnails.of(originalFileName)
                                            .size(150, 150)
                                            //按照比列压缩，高度不一定是150，如果强制按照分辨率压缩，需要调用
                                            //.keepAspectRatio(false)
                                            .toFile(basePath + finalPath + "_" + fileName + suffix);
                                    }
                                } catch (Exception e) {
                                    log.warn("文件压缩失败:{}", e.getMessage());
                                }
                            });
                        }
                    } catch (IOException e) {
                        log.warn("文件上传失败:{}", e.getMessage());
                        throw new ValidateException("文件上传失败:" + e.getMessage());
                    }
                }
            }
        }
        return fileNames;
    }

    /**
     * 设置默认上传路径
     */
    public static String setDefaultPath(String path) {
        String finalPath;
        if (OthersUtils.isEmpty(path)) {
            finalPath = "/images/others/";
        } else {
            if (path.contains("%")) {
                try {
                    finalPath = URLDecoder.decode(path, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    log.warn("路径参数有误:{}", e.getMessage());
                    throw new ValidateException("路径参数有误");
                }
            } else {
                finalPath = path;
            }
        }
        return finalPath;
    }

    /**
     * 获取文件默认保存路径，windows和linux保存的路径不一样
     */
    public static String getFileSaveParentPath() {
        String parentPath;
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            parentPath = request.getSession().getServletContext().getRealPath("/");
        } else {
            parentPath = commonProperties.getStaticDir();
        }
        return parentPath;
    }

    /**
     * 从请求中读取数据
     */
    public static String readDataFromRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new ValidateException(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * 获取项目的根路径
     */
    public static String getClassRootPath() {
        return Objects.requireNonNull(OthersUtils.class.getClassLoader().getResource("")).getPath();
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof String) {
            return "".equals(object);
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        } else if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map) object).size() == 0;
        } else {
            return false;
        }
    }

    /**
     * xml转map
     */
    public static Map<String, Object> xmlToMap(String xml) {
        Map<String, Object> data = new HashMap<>(10);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        InputStream stream;
        Document doc;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            doc = documentBuilder.parse(stream);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new ValidateException(e.getMessage());
        }
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }
        try {
            stream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    /**
     * map转xml
     */
    public static String mapToXml(Map<String, Object> data) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.warn(e.getMessage());
            throw new ValidateException("解析失败");
        }
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                Element filed = document.createElement(entry.getKey());
                filed.appendChild(document.createTextNode(entry.getValue().toString()));
                root.appendChild(filed);
            }
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            log.warn(e.getMessage());
            throw new ValidateException("解析失败");
        }
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            log.warn(e.getMessage());
            throw new ValidateException("解析失败");
        }
        String output = writer.getBuffer().toString();
        try {
            writer.close();
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return output;
    }

    /**
     * 产生随机数(纯数字)
     */
    public static String createRandom(int length) {
        return createRandom(true, length);
    }

    /**
     * 产生随机数(字母+数字)
     */
    public static String createRandom(boolean numberFlag, int length) {
        String strTable = numberFlag ? "0123456789" : "0123456789abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        StringBuilder stringBuffer = new StringBuilder();
        while (stringBuffer.length() < length) {
            double dblR = Math.random() * len;
            int intR = (int) Math.floor(dblR);
            char c = strTable.charAt(intR);
            if (('0' <= c) && (c <= '9')) {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 字符串转Integer
     */
    public static Integer toInteger(String value) {
        BigDecimal decimal = new BigDecimal(value);
        return decimal.intValue();
    }

    /**
     * 字符串转Integer
     */
    public static Integer toInteger(Float value) {
        BigDecimal decimal = new BigDecimal(value);
        return decimal.intValue();
    }

    /**
     * 字符串转指定长度的Float
     */
    public static Float toFloat(String value, int length) {
        BigDecimal decimal = new BigDecimal(value);
        return decimal.setScale(length, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * Float转指定长度的Float
     */
    public static Float toFloat(Float value, int length) {
        BigDecimal decimal = new BigDecimal(value);
        return decimal.setScale(length, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 字符串转指定长度的Double
     */
    public static Double toDouble(String value, int length) {
        BigDecimal decimal = new BigDecimal(value);
        return decimal.setScale(length, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
