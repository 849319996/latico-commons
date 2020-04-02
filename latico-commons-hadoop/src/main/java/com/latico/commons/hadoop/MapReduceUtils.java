package com.latico.commons.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * <PRE>
 * map/reduce 工具
 *
 * 使用FileInputFormat.setInputPaths设置多文件路径输入
 * 使用MultipleInputs.addInputPath()方法添加输入路径，输入类型和Mapper类,支持更灵活的不同的输入和Mapper来处理数据。
 *
 * 使用FileOutputFormat.setOutputPath设置单文件输出
 * 使用MultipleOutputs类来控制输出多路径。重写Reducer的setup()和cleanup()方法
 * </PRE>
 *
 * @author: latico
 * @date: 2020-03-25 10:28
 * @version: 1.0
 */
public class MapReduceUtils {

    /**
     * 等待完成
     * @param job
     * @throws Exception
     */
    public static boolean waitForCompletion(Job job) throws Exception {
        return job.waitForCompletion(true);
    }

    /**
     * 创建一个MapReduce的配置对象
     * @param hdfsUrl 地址
     * @param hdfsUser 用户名
     * @param jobName 作业名称
     * @param inputFormatClass 设置起始的输入格式处理类，可以为空（默认使用，{@link TextInputFormat}），将输入的数据集切割成小数据集inputSplits，每一个InputSplit将 由一个Mapper负责处理。此外inputFormat中还提供一个RecordReader的 实现, 将一个InputSplit解 析成《key,value》 对提供给 map 函数。
     *                          默认建议使用，TextInputFormat(针 对文本文件，按行将文本文件切割成InputSplits, 并用LineRecordReader将InputSplit解 析成 《key,value》 对，key是行在文件中的位置，value是文件中的一行)
     * @param outputFormatClass 设置结束的输出格式处理类， 以为空（默认使用，{@link TextOutputFormat}），提供一个 RecordWriter 的实现，负责输出最终结果，
     *                          TextOutputFormat(用 LineRecordWriter 将最终结果写成纯文件文件,每个《key,value》对一行，key 和 value 之间用 tab 分隔)
     * @param outputKeyClass 输出的最终结果中 key 的类型
     * @param outputValueClass 输出的最终结果中 value 的类型
     * @param mapOutputKeyClass 可以为空，设定 map 函数输出的中间结果中 key 的类型，如果用户没有设定的话，使用OutputKeyClass
     * @param mapOutputValueClass 可以为空，设定 map 函数输出的中间结果中，value 的类型 如果用户没有设定的话，使用 OutputValuesClass
     * @param mapperClass Mapper 类，实现 map 函数，完成输入的 《key,value》 到中间结果的映射
     * @param reducerClass Reducer 类，实现 reduce 函数，对中间结果做合并，形成最终结果
     * @param outputFilePath 输出文件路径，单个
     * @param inputFilePaths 输入路径，支持多个
     * @return
     */
    public static Job getJob(String hdfsUrl,
                                 String hdfsUser,
                                 String jobName,
                                 Class<? extends InputFormat> inputFormatClass,
                                 Class<? extends OutputFormat> outputFormatClass,
                                 Class<? extends WritableComparable> outputKeyClass,
                                 Class<? extends WritableComparable> outputValueClass,
                                 Class<? extends WritableComparable> mapOutputKeyClass,
                                 Class<? extends WritableComparable> mapOutputValueClass,
                                 Class<? extends Mapper> mapperClass,
                                 Class<? extends Reducer> reducerClass,
                                 String outputFilePath,
                                 String... inputFilePaths) throws IOException {

        //检测初始化
        if (inputFormatClass == null) {
            inputFormatClass = TextInputFormat.class;
        }

        if (outputFormatClass == null) {
            outputFormatClass = TextOutputFormat.class;
        }

        if (mapOutputKeyClass == null) {
            mapOutputKeyClass = outputKeyClass;
        }

        if (mapOutputValueClass == null) {
            mapOutputValueClass = outputValueClass;
        }

        //创建配置对象
        Job job = new Job(HdfsUtils.getConfiguration(hdfsUrl));
        job.setUser(hdfsUser);
        job.setJobName(jobName);

        //设置起始的输入格式处理类
        job.setInputFormatClass(inputFormatClass);
        //设置结束的输出格式处理类
        job.setOutputFormatClass(outputFormatClass);

        //设置mapper的输出格式
        job.setMapOutputKeyClass(mapOutputKeyClass);
        job.setMapOutputValueClass(mapOutputValueClass);

        //设置最终的输出格式
        job.setOutputKeyClass(outputKeyClass);
        job.setOutputValueClass(outputValueClass);

        //设置业务处理类
        job.setMapperClass(mapperClass);
        job.setReducerClass(reducerClass);

        //设置文件的输入输出路径
        setFilePath(job, outputFilePath, inputFilePaths);

        return job;
    }

    /**
     * 设置文件路径
     * @param job
     * @param outputFilePath
     * @param inputFilePaths
     */
    public static void setFilePath(Job job, String outputFilePath, String[] inputFilePaths) throws IOException {
        Path[] inPaths = new Path[inputFilePaths.length];
        for (int i = 0; i < inputFilePaths.length; i++) {
            inPaths[i] = new Path(inputFilePaths[i]);
        }
        FileInputFormat.setInputPaths(job, inPaths);
        FileOutputFormat.setOutputPath(job, new Path(outputFilePath));
    }

}
