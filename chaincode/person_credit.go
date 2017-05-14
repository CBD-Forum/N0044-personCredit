package main

import (
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// SimpleChaincode example simple Chaincode implementation
type SimpleChaincode struct {
}

var resumeInfoMap map[string][]byte //保存用户简历信息字典

//初始方法
func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {

	return shim.Success(nil)
}

func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("调用Invoke方法")
	function, args := stub.GetFunctionAndParameters()
	if function != "invoke" {
		return shim.Error("Unknown function call")
	}
	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting at least 2")
	}

	if args[0] == "saveResumeInfo" {
		// 保存简历信息到state中
		return t.saveResumeInfo(stub, args)
	} else if args[0] == "updateResumeInfo" {
		// 更新简历信息到state中
		return t.updateResumeInfo(stub, args)
	} else if args[0] == "queryResumeInfo" {
		// 查询简历信息
		return t.queryResumeInfoByIdCard(stub, args)
	}

	return shim.Error("Invalid invoke function name. Expecting \"saveResumeInfo\" \"updateResumeInfo\" \"queryResumeInfo\"")
}

//保存简历信息
func (t *SimpleChaincode) saveResumeInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var err error
	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}
	//TODO 唯一标识可能是身份证或者其他ID
	idCard := args[1]
	resumeInfo := args[2] //加密简历信息

	fmt.Println("调用saveResumeInfo方法,参数：" + idCard + "," + resumeInfo)
	//判断该简历信息是否已存在
	resumeInfoMap, err = getResumeInfoMap(stub)
	if err != nil {
		return shim.Error("获取简历数据失败！")
	}
	_, ok := resumeInfoMap[idCard]
	if ok {
		return shim.Error("保存错误，用户" + idCard + "已存在，无法保存！")
	}

	resumeInfoMap[idCard] = []byte(resumeInfo)
	jsonResumeInfoMapBytes, err := json.Marshal(&resumeInfoMap)
	if err != nil {
		return shim.Error("转换json格式数据错误！")
	}
	//将简历信息map放入state中
	err = stub.PutState("resumeInfoMap", jsonResumeInfoMapBytes)
	if err != nil {
		return shim.Error("保存简历信息失败！")
	}

	return shim.Success(nil)
}

//更新简历信息
func (t *SimpleChaincode) updateResumeInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var err error
	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}

	idCard := args[1]
	resumeInfo := args[2] //简历信息

	//判断该简历信息是否已存在
	resumeInfoMap, _ = getResumeInfoMap(stub)
	data, ok := resumeInfoMap[idCard]
	if !ok {
		return shim.Error("用户：" + idCard + "不存在，无法更新！")
	}

	jsonQues := "{\"idCard\":\"" + idCard + "\",\"resumeInfo\":\"" + resumeInfo + "\"}"
	fmt.Printf("Query jsonQues:%s\n", jsonQues)

	resumeInfoMap[idCard] = []byte(string(data) + "," + resumeInfo)
	jsonResumeInfoMapBytes, _ := json.Marshal(&resumeInfoMap)
	//将简历信息map放入state中
	err = stub.PutState("resumeInfoMap", jsonResumeInfoMapBytes)
	if err != nil {
		return shim.Error("更新简历信息失败！")
	}

	return shim.Success(nil)
}

// 查询简历信息
func (t *SimpleChaincode) queryResumeInfoByIdCard(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	idCard := args[1]

	//jsonResp := "{\"idCard\":\"" + idCard + "\",\"priKey\":\"" + priKey + "\"}"
	fmt.Printf("Query Response:%s\n", idCard)

	resumeInfoMap, err = getResumeInfoMap(stub)
	if err != nil {
		return shim.Error("获取简历数据错误！")
	}

	resumeInfo, ok := resumeInfoMap[idCard]
	if !ok {
		return shim.Error("用户:" + idCard + "数据不存在！")
	}

	jsonResp := "{\"idCard\":\"" + idCard + "\",\"resumeInfo\":\"" + string(resumeInfo) + "\"}"
	fmt.Printf("Query Response:%s\n", jsonResp)

	return shim.Success(resumeInfo)

}

//获取stub中ResumeInfoMap
func getResumeInfoMap(stub shim.ChaincodeStubInterface) (map[string][]byte, error) {
	resumeInfoMapBytes, err := stub.GetState("resumeInfoMap")
	if err != nil {
		return nil, err
	}
	if len(resumeInfoMapBytes) == 0 {
		resumeInfoMap = make(map[string][]byte)
	} else {
		err = json.Unmarshal(resumeInfoMapBytes, &resumeInfoMap)
	}
	if err != nil {
		return nil, err
	}
	return resumeInfoMap, err
}

func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
