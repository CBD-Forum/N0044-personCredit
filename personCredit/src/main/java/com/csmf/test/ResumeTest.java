package com.csmf.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;

import com.csmf.common.StatusCode;
import com.csmf.dao.ISeqNoDAO;
import com.csmf.dao.SecretKeyDAO;
import com.csmf.dto.AnalysisResult;
import com.csmf.dto.Resume;
import com.csmf.dto.SerialNumberConfigDto;
import com.csmf.dto.send.SendAllSkills;
import com.csmf.dto.send.SendCertificate;
import com.csmf.dto.send.SendEducation;
import com.csmf.dto.send.SendKill;
import com.csmf.dto.send.SendPersonInfo;
import com.csmf.dto.send.SendProject;
import com.csmf.dto.send.SendTrain;
import com.csmf.dto.send.SendWork;
import com.csmf.resume.JobResume;
import com.csmf.resume.ResumeBase;
import com.csmf.resume.ResumePasreFactory;
import com.csmf.resume.ResumeUpload;
import com.csmf.resume.auth.ResumeAnalysis;
import com.csmf.service.CompanyAdminService;
import com.csmf.service.FabricService;
import com.csmf.service.PersonInfoService;
import com.csmf.service.SecretKeyService;
import com.csmf.service.impl.FabricServiceImpl;
import com.csmf.util.Base64;
import com.csmf.util.RSA;
import com.csmf.util.SerialNumberType;
import com.fabric.java.core.sdk.FabricClient;
import com.google.gson.JsonObject;

import net.sf.json.JSONObject;


public class ResumeTest extends BaseTest{

	@Resource
	JobResume jobResume;
	@Resource
	ResumePasreFactory resumePasreFactory;
	@Resource
	CompanyAdminService companyAdminService;
	@Resource 
	FabricService fabricService;
	@Resource
	ISeqNoDAO seqNoDAO;
	@Resource
	SecretKeyDAO secretKeyDAO;
	@Resource
	ResumeUpload resumeUpload;
	@Resource
	SecretKeyService secretKeyService;
	@Resource
	PersonInfoService personInfoService;
	
	@Test
	public void jobResume() throws Exception{
		Resume resume = jobResume.parseDoc(new FileInputStream(new File("/Users/pact/Downloads/我的简历.doc")));
		System.out.println(resume.getHighestEducation());
	}
	
	@Test
	public void testc(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", "123456789");
		params.put("passwd", "sbSVdPEuiiod");
		params.put("memberId", "0002");
		params.put("sid", "12345");
		params.put("prikey", "123457");
		params.put("pubKey", "123457");
		//Map<String,Object> dataMap=secretKeyDAO.querySecretKeyById("12921234567890780");
		//System.out.println(dataMap.toString());
		//secretKeyDAO.updateSecretKey(params);
		//VALUES (#{sid},#{passwd},#{prikey},#{pubKey},#{memberId},#{id},SYSDATE(),SYSDATE())
		//secretKeyDAO.insertSecretKey(params);
		try {
			secretKeyService.updateSecretKey(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() throws Exception{
		
		InputStream in = new FileInputStream(new File("/Users/pact/Downloads/我的简历1.doc"));
		List<Map<String,Object>> list = companyAdminService.resumeAnalysis(in, "51job", "123456189098765828", "123456189098765828");
	}
	
	@Test
	public void test4() throws Exception{
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyName", "12345678");
		param.put("companyId", seqNoDAO.getFlwNo("accountNo", "COM"));				
		param.put("licenseImage", "zwt");				
		param.put("address", "深圳");
		//param.put("remark", jsonObject.getString("remark"));
		param.put("licenseNum", "12345678");
		param.put("companyNo", "12345678");
		param.put("personNo", "zwt");
		
		companyAdminService.saveCompanyInfo(param);
	}
	
	
	@Test
	public void test9(){
		ZipOutputStream out = null;
		
		String companyNo = "123456789098765409";
		
		File files = new File("/Users/pact/Downloads/我的简历2.zip");  
		
		
		InputStream in = null;  
        try {  
 
            in = new FileInputStream(new File("/Users/pact/Downloads/123456189098765464.zip"));  
            byte[] data = new byte[1024];
            int len =0;
            FileOutputStream bufOs = new FileOutputStream(files);
            while ((len = in.read(data, 0, data.length)) > 0) {
                bufOs.write(data, 0, len);
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                // 关闭输入流  
                in.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
		//resumeUpload.resumeZipParse(files,companyNo);
	}
	
	
	@Test
	public void test1() throws Exception{
		
		SerialNumberConfigDto dto = new SerialNumberConfigDto();
    	dto.setDefaultValue("SE");
    	dto.setLength(12);
    	dto.setSeqNoName("secretKey");
    	dto.setType(SerialNumberType.NULL);
    	String no = seqNoDAO.getFlwNo(dto);
    	Map<String,Object> saveMap = null;
    	try {
    		 saveMap=fabricService.registerFarbric("12901234567890780");
    		 System.out.println(saveMap.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//Thread.currentThread().join();
		//FabricClient client = new FabricClient();
		
//		
//		String passwd = client.registerUser("zwtfgg");
//		
//		System.out.println(passwd);
	}
	
	@Test
	public void testQueryResume() throws Exception{
		
		Map<String,Object> data =  fabricService.queryFabricResume("123456189098765828","123456189098765828");
		
		System.out.println(data.get("json"));
		
		
	}
	
	@Test
	public void testSaveResume(){
		SendPersonInfo personInfo = new SendPersonInfo();
		personInfo.setName("何福暖");
		personInfo.setIdentityNum("350322199301161154");
		personInfo.setBornDate("1993/06/28");
		personInfo.setOrigin("001");
		personInfo.setGender("M");
		personInfo.setHighestEducation("本科");
		personInfo.setPolitical("5001");
		personInfo.setNation("00");
		personInfo.setMarry("F");
		
		
		List<SendWork> works = new ArrayList<SendWork>();
		SendWork work = new SendWork();
		work.setDimissionTime("2017/02/02");
		work.setEntryTime("2015/5");
		work.setCompanName("文思海辉系统有限公司");
		work.setPosition("软件工程师");
		work.setTrade("计算机软件");
		work.setWorkDescription(("负责前后台开发与设计"));
		work.setDepartment("金融事业部");
		works.add(work);
		
		work.setDimissionTime("2015/4");
		work.setEntryTime("2014/5");
		work.setCompanName("惠州风云软件科技有限公司");
		work.setPosition("软件工程");
		work.setTrade("计算机软件");
		work.setWorkDescription(("负责web项目的后端开发与设计"));
		work.setDepartment("开发部");
		works.add(work);
		
		List<SendProject> projects = new ArrayList<SendProject>();
		SendProject project = new SendProject();
		project.setProjectName("平安信用险融资在线管理平台");
		project.setProjectBeginTime("2016/1");
		project.setProjectEndTime("2017/02/02");
		project.setProjectDescription("本人担任项目中的组长职位，负责人员安排以及项目进度的把控并负责部分模块的开发");
		project.setCompanyName("companyName");
		project.setResponsibility("本人担任项目中的组长职位，负责人员安排以及项目进度的把控并负责部分模块的开发");
		List<SendKill> skillList = new ArrayList<SendKill>();
		for(int i=199;i<204;i++){
			SendKill skill = new SendKill();
			skill.setSkillDataNum("skill"+i);
			skill.setSkillDataType("type"+i);
			skillList.add(skill);
		}
		project.setSkillId(skillList);
		project.setWorkId("workId");
		projects.add(project);
		projects.add(project);
		
		// 查询教育并封装成bean
		List<SendEducation> educations = new ArrayList<SendEducation>();
		SendEducation education = new SendEducation();
		education.setAdmissionTime("2017/02/02");
		education.setCredentials("credentials");
		education.setEducationBackground("educationBackground");
		education.setGraduationTime("2017/02/02");
		education.setMajor("major");
		education.setSchoolName("schoolName");
		//education.setStatus('T');
		educations.add(education);
		
		List<SendTrain> trains = new ArrayList<SendTrain>();
		SendTrain train = new SendTrain();
		train.setCompanName("companName");
		train.setStartTime("2017/02/02");
		train.setEndTime("2017/02/02");
		train.setTrainingDescription("trainingDescription");
		train.setTrainingName("trainingName");
		trains.add(train);
		
		
		List<SendCertificate> certs = new ArrayList<SendCertificate>();
		SendCertificate cert = new SendCertificate();
		cert.setCertificateTime("2017/02/02");
		cert.setCertificateType("certificateType");
		cert.setName("name");
		certs.add(cert);
		
		List<SendAllSkills> skills = new ArrayList<SendAllSkills>();
		SendAllSkills skill = new SendAllSkills();
		skill.setProficiency("proficiency");
		skill.setSkillNum("skillNum");
		skill.setSkillType("skillType");
		skills.add(skill);
		
		Map map = new HashMap();
		map.put(StatusCode.JSON_INFO, personInfo);
		map.put(StatusCode.JSON_EDUCATION, educations);
		map.put(StatusCode.JSON_WORK_EXPE, works);
		map.put(StatusCode.JSON_PROJECT_EXPE, projects);
		map.put(StatusCode.JSON_ALL_SKILL, skills);
		map.put(StatusCode.JSON_TRAIN, trains);
		map.put(StatusCode.JSON_CERT, certs);
		
		JSONObject obj = JSONObject.fromObject(map);
		
		
		FabricServiceImpl impl = new FabricServiceImpl();
		Map resultMap = impl.BlockChainToMap(obj.toString());
		JSONObject obj1 = JSONObject.fromObject(resultMap);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", "123456189098765828");
		param.put("passwd", "gqNKaOuVGDEC");
		param.put("login", "123456189098765828");
		RSA rsa = RSA.create();
		String s = rsa.encodeByPublicKey(obj1.toString(), "30819F300D06092A864886F70D010101050003818D0030818902818100A6E7F864C084D30DC61DDFCFA8B334BDE8F6980B8C4E938AAC4B55032DA7580C440203DC815CB7D7FF1CCE733CDF76470502FBCC56EAE5D45B3D299E9EC6A981F4236DDDC59585AF5A315F9597E76490F86EDED86FB3AE3BF8857BAAA6103D59383D43F1AC953AFC8F7F789111BB1B18CE3C10969FECCACD61B5B72B2658515D0203010001");
		param.put("json", s);
		//impl.saveResume(param);
	
		
	}
	
	
	@Test
	public void test100() throws Exception{
		Map map = new HashMap();
		map.put("memberId", "M000000000172");
		map.put("indentityNum", "124124345634123456");
			personInfoService.updateIdentityNum(map);
	}
	
	
	
	
	
	
}
