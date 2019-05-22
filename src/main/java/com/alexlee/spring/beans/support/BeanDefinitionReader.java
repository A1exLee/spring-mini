package com.alexlee.spring.beans.support;

import com.alexlee.spring.annotion.Component;
import com.alexlee.spring.annotion.Controller;
import com.alexlee.spring.beans.config.BeanDefinition;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Properties;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/5 16:28
 */
public class BeanDefinitionReader {
    private static final Logger logger = LoggerFactory.getLogger(BeanDefinitionReader.class);
    private Properties props = new Properties();
    private List<String> classesRegistry = Lists.newArrayList();
    public BeanDefinitionReader(String configLocation) {


        InputStream is = null;
        try {
            URI uri = BeanDefinitionReader.class.getClassLoader().getResource("").toURI();
            String url=uri.getPath()+configLocation;
            File file = new File(url);
            if (!file.exists() || !file.isFile()) {
                logger.error("文件不存在:"+file.getPath());
                return;
            }
            is = this.getClass().getResourceAsStream("/"+configLocation);
            props.load(is);
            doScan(props.getProperty("scanPackage"));
        } catch (Exception e) {
            logger.error("发生异常", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error("关闭流发生异常", e);
            }
        }
    }

    public Properties getProps() {
        return props;
    }


    private void doScan(String packageName) {
        if (packageName.trim().isEmpty()) {
            System.out.println("扫描包名为空");
            return;
        }
        String url = packageName.replaceAll("\\.", "/");
        File file = new File(this.getClass().getClassLoader().getResource(url).getPath());
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    doScan(packageName + "." + f.getName());
                }
                if (f.getName().endsWith(".class")) {
                    classesRegistry.add(packageName + "." + f.getName().split("\\.")[0]);
                }
            }
        } else {
            System.out.println("扫描的包不存在");
        }
    }

    public List<BeanDefinition> loadBeanDefinition() {
        List<BeanDefinition> definitions = Lists.newArrayList();
        try {
            for (String s : classesRegistry) {
                Class clazz = Class.forName(s);
                if (clazz.isInterface()) {
                    continue;
                }
                if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Controller.class)) {
                    definitions.add(createBeanDefinition(lowerFirstCase(clazz.getSimpleName()), s));
                    for (Class anInterface : clazz.getInterfaces()) {
                        definitions.add(createBeanDefinition(lowerFirstCase(anInterface.getSimpleName()), s));
                    }
                    Class superClz = clazz.getSuperclass();
                    while (Object.class != superClz) {
                        definitions.add(createBeanDefinition(lowerFirstCase(superClz.getSimpleName()), s));
                        superClz = superClz.getSuperclass();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("创建beanDefinition发生异常", e);
        }
        return definitions;
    }

    private BeanDefinition createBeanDefinition(String lowerFirstCase, String s) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(s);
        beanDefinition.setFactoryBeanName(lowerFirstCase);
        return beanDefinition;
    }

    /**
     * 首字母小写
     *
     * @param simpleName
     * @return
     */
    private String lowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

}
