<template>
  <div>
    <el-container>
      <el-main style="padding: 10px 0px 0px 0px;display:flex;justify-content:space-between;align-items: center">
        <div style="text-align: left">
        <el-form :model="resource" :rules="rules" ref="addResourceForm" style="display: inline;">
          <el-row>
            <el-col :span="8">
              <div>
              <el-form-item label="资源月:" prop="month">
                <el-date-picker
                  v-model="resource.month"
                  size="mini"
                  style="width: 130px"
                  type="month"
                  value-format="yyyyMM"
                  placeholder="资源月"
                  id="monthInput">
                </el-date-picker>
              </el-form-item>
              </div>
            </el-col>
            <el-col :span="8">
              <div>
                <el-form-item label="项目:" prop="projectId">
                  <el-select v-model="resource.projectId" style="width: 200px" size="mini" placeholder="请选择项目">
                    <el-option
                      v-for="item in projects"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="8">
              <div>
                <el-form-item>
                <el-button type="primary" size="mini" icon="el-icon-plus" @click="saveResource">
                    保存
                </el-button>
                </el-form-item>
              </div>
            </el-col>
          </el-row>
          <el-table
            :data="detailResources"
            v-loading="tableLoading"
            row-key="employeeId"
            border
            stripe
            size="mini"
            style="width: 100%">
            <el-table-column
              prop="employeeName"
              align="left"
              fixed
              label="姓名"
              width="200px">
            </el-table-column>
            <el-table-column
              prop="resourceLevel"
              width="200px"
              align="left"
              label="职称">
            </el-table-column>
            <el-table-column
              prop="resource"
              align="left"
              label="预计资源数"
              width="200px">
            </el-table-column>
            <el-table-column
              prop="dateRange"
              align="left"
              label="开始-结束日期"
              width="200px">
              <template slot-scope="scope">{{scope.row.dateRange[0]}} - {{scope.row.dateRange[1]}}</template>
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="105">
              <template slot-scope="scope">
                <el-button @click="showAddDetailResource(scope.$index,scope.row)" style="padding: 3px 4px 3px 4px;margin: 2px"
                           size="small">编辑
                </el-button>
                <el-button type="danger" style="padding: 3px 4px 3px 4px;margin: 2px" size="small"
                           @click="deleteResource(scope.row)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
        <div style="display: flex;justify-content: space-between;margin: 2px;padding-top:10px;">
            <el-button type="danger" icon="el-icon-plus" size="mini" @click="showAddDetailResource">添加资源项
            </el-button>
          </div>
        </div>
      </el-main>
    </el-container>
    
    <el-form :model="detailResource" :rules="rules" ref="addDetailResourceForm" style="margin: 0px;padding: 0px;">
      <div style="text-align: left">
        <el-dialog
          :title="dialogTitle"
          style="padding: 0px;"
          :close-on-click-modal="false"
          :visible.sync="dialogVisible"
          width="70%">
          <el-row>
            <el-col :span="8">
              <div>
                <el-form-item label="姓名:" prop="name">
                  <!-- <el-select v-model="detailResource.employeeId" style="width: 150px" size="mini" placeholder="请选择人员">
                    <el-option
                      v-for="item in employees"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select> -->
                  <el-autocomplete
                    v-model="detailResource.employeeName"
                    :fetch-suggestions="querySearch"
                    placeholder="请输入人员姓名"
                    size="small"
                    @select="handleSelect">
                  </el-autocomplete>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="8">
              <div>
                <el-form-item label="资源数:" prop="projectCode">
                  <el-input prefix-icon="el-icon-edit" v-model="detailResource.resource" size="small" style="width: 200px"
                            placeholder="请输入资源数"></el-input>
                </el-form-item>
              </div>
            </el-col>
            </el-row>
            <el-row>
            <el-col :span="16">
              <div>
                <el-form-item label="资源开始结束日期:" prop="dateRange">
                  <el-date-picker
                    v-model="detailResource.dateRange"
                    type="daterange"
                    value-format="yyyy-MM-dd"
                    size="small"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
                  </el-date-picker>
                </el-form-item>
              </div>
            </el-col>
          </el-row>
          
          <span slot="footer" class="dialog-footer">
            <el-button size="mini" @click="cancelEidt">取 消</el-button>
            <el-button size="mini" type="primary" @click="addDetailResource">确 定</el-button>
          </span>
        </el-dialog>
      </div>
    </el-form>
  </div>
</template>
<script>
import queryString from 'querystring'
  export default {
    data() {
      return {
        resource: {
          month: '',
          projectId: '',
          details: []
        },
        detailResources: [],
        employees: [],
        dialogTitle: '',
        projectStatus: [{id: 0, name: '未开始'}, {id: 1, name: '进行中'}, {id: 6, name: '已结束'}],
       
        totalCount: -1,
        currentPage: 1,
       
        dialogVisible: false,
        tableLoading: false,
        advanceSearchViewVisible: false,
        showOrHidePop: false,
        showOrHidePop2: false,
        projects: [],
        timeout: null,
        detailResource: {
          //id: '',
          employeeId: '',
          resource: '',
          dateRange: [],
          dateRange: '',
          resourceLevel: ''
        },
        rules: {
          month: [{required: true, message: '必填:资源月', trigger: 'blur'}],
          projectId: [{required: true, message: '必填:项目', trigger: 'blur'}]
        },
        editResourceId: null,
        editDetailFlag: false,
        editRowIndex: null
      };
    },
    mounted: function () {
      this.initData();
      this.loadResourcesDetails();
    },
    methods: {
      fileUploadSuccess(response, file, fileList){
        if (response) {
          this.$message({type: response.status, message: response.msg});
        }
        this.loadEmps();
        this.fileUploadBtnText = '导入数据';
      },
      fileUploadError(err, file, fileList){
        this.$message({type: 'error', message: "导入失败!"});
        this.fileUploadBtnText = '导入数据';
      },
      beforeFileUpload(file){
        this.fileUploadBtnText = '正在导入';
      },
      exportResource(){
//        var iframe = document.createElement("iframe");
//        iframe.style.display = 'none';
//        iframe.src = "/employee/basic/exportEmp";
//        iframe.onload=function () {
//          document.body.removeChild(iframe);
//        }
//        document.body.appendChild(iframe);
        window.open("/project/basic/exportProject", "_parent");
      },
      cancelSearch(){
        this.advanceSearchViewVisible = false;
        this.emptyResourceData();
        this.beginDateScope = '';
        this.loadResourcesDetails();
      },
      showAdvanceSearchView(){
        this.advanceSearchViewVisible = !this.advanceSearchViewVisible;
        this.keywords = '';
        if (!this.advanceSearchViewVisible) {
          this.emptyResourceData();
          this.beginDateScope = '';
          this.loadResourcesDetails();
        }
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      deleteManyResource(){
        this.$confirm('此操作将删除[' + this.multipleSelection.length + ']条数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var ids = '';
          for (var i = 0; i < this.multipleSelection.length; i++) {
            ids += this.multipleSelection[i].id + ",";
          }
          this.doDelete(ids);
        }).catch(() => {
        });
      },
      deleteResource(row){
        this.$confirm('此操作将永久删除[' + row.name + '], 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.doDelete(row.id);
        }).catch(() => {
        });
      },
      doDelete(ids){
        this.tableLoading = true;
        var _this = this;
        this.deleteRequest("/resource/est/del/" + ids).then(resp=> {
          _this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.$message({type: data.status, message: data.msg});
            _this.loadResourcesDetails();
          }
        })
      },
      keywordsChange(val){
        if (val == '') {
          this.loadResourcesDetails();
        }
      },
      searchResource(){
        this.loadResourcesDetails();
      },
      currentChange(currentChange){
        this.currentPage = currentChange;
        this.loadPesources();
      },
      loadResourcesDetails(){
        var _this = this;
        this.editResourceId = this.$route.params.editResourceId;
        if(this.editResourceId != null && this.editResourceId != ''){
          this.tableLoading = true;
          this.getRequest("/resource/est/detailList?resourceId=" + this.editResourceId).then(resp=> {
            this.tableLoading = false;
            if (resp && resp.status == 200) {
              var data = resp.data;
              _this.resource = data.resource;
              document.getElementById("monthInput").disabled = true;
              var detailListData = [];
              for (let i = 0; i < data.detailResources.length; i++) {
                var obj = {};
                obj.employeeId = data.detailResources[i].employeeId;
                obj.employeeName = data.detailResources[i].employeeName;
                obj.resourceLevel = data.detailResources[i].resourceLevel;
                obj.resource = data.detailResources[i].resource;
                var dateRangeArray = [];
                dateRangeArray[0] = data.detailResources[i].startDate;
                dateRangeArray[1] = data.detailResources[i].endDate;
                obj.dateRange = dateRangeArray;
                detailListData[i] = obj;
              }
              _this.detailResources = detailListData;
            }
          })
        }
      },
      saveResource(){
        var _this = this;
        _this.resource.details = _this.detailResources;
        this.$refs['addResourceForm'].validate((valid) => {
          if (valid) {
            let params = {};
            params.month = this.resource.month;
            params.projectId = this.resource.projectId;
            params.details = this.resource.details;

            if (this.resource.id) {
              //更新
              this.tableLoading = true;
              this.putRequest("/resource/est/update", this.resource, this.detailResources).then(resp=> {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.$message({type: data.status, message: data.msg});
                  _this.dialogVisible = false;
                  _this.emptyResourceData();
                  _this.loadPesources();
                }
              })
            } else {
              //添加
              this.tableLoading = true;
              this.postRequestJson("/resource/est/add",params).then(resp=> {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.$message({type: data.status, message: data.msg});
                  _this.dialogVisible = false;
                  _this.emptyResourceData();
                  _this.loadPesources();
                }
              })
            }
          } else {
            return false;
          }
        });
      },
      cancelEidt(){
        this.dialogVisible = false;
        this.emptyResourceData();
      },
      showDepTree(){
        this.showOrHidePop = !this.showOrHidePop;
      },
      showDepTree2(){
        this.showOrHidePop2 = !this.showOrHidePop2;
      },
      handleNodeClick(data) {
        this.project.departmentName = data.name;
        this.project.departmentId = data.id;
        this.showOrHidePop = false;
        this.depTextColor = '#606266';
      },
      handleNodeClick2(data) {
        this.project.departmentName = data.name;
        this.project.departmentId = data.id;
        this.showOrHidePop2 = false;
        this.depTextColor = '#606266';
      },
      initData(){
        var _this = this;
        this.getRequest("/resource/est/basicdata").then(resp=> {
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.employees = data.employees;
            _this.projects = data.projects;
          }
        })
      },
      showEditResourceView(row){
        console.log(row)
        this.dialogTitle = "编辑项目";
        this.project = row;
        this.project.dateRange = this.formatDate(row.dateRange);
        this.project.endDate = this.formatDate(row.endDate);
        //this.project.id = row.project.id;
        //this.project.name = row.project.name;
        //this.project.departmentId = row.departmentId;
        //this.project.departmentName = row.departmentName;
        this.project.status = row.status;
//        delete this.project.department;
//        delete this.project.jobLevel;
//        delete this.project.position;
//        delete this.project.nation;
//        delete this.project.politicsStatus;
        this.dialogVisible = true;
      },
      showAddDetailResource(index, row){
        this.dialogTitle = "添加资源项";
        this.dialogVisible = true;
        var _this = this;
        _this.editDetailFlag = false;
        if(row != null){
          this.dialogTitle = "编集资源项";
          _this.detailResource.employeeId = row.employeeId;
          _this.detailResource.employeeName = row.employeeName;
          _this.detailResource.resourceLevel = row.resourceLevel;
          _this.detailResource.resource = row.resource;
          _this.detailResource.dateRange = row.dateRange;
          _this.editDetailFlag = true;
          _this.editRowIndex = index;
        }
        

      },
      addDetailResource(){
        var _this = this;
        if(!this.editDetailFlag){
          let detail = {};
          detail.resource = _this.detailResource.resource;
          detail.employeeId = _this.detailResource.employeeId;
          detail.employeeName = _this.detailResource.employeeName;
          detail.resourceLevel =  _this.detailResource.resourceLevel;
          detail.dateRange = _this.detailResource.dateRange;
          _this.detailResources.push(detail);
        }else{
          _this.detailResources[this.editRowIndex].employeeId = _this.detailResource.employeeId;
          _this.detailResources[this.editRowIndex].resource = _this.detailResource.resource;
          _this.detailResources[this.editRowIndex].employeeName = _this.detailResource.employeeName;
          _this.detailResources[this.editRowIndex].resourceLevel = _this.detailResource.resourceLevel;
          _this.detailResources[this.editRowIndex].dateRange = _this.detailResource.dateRange;
        }


        this.dialogVisible = false;
        this.emptyDetailResourceData();
      },
      emptyDetailResourceData(){
        this.detailResource = {
          employeeId: '',
          employeeName: '',
          resourceLevel: '',
          resource: '',
          dateRange: [],
          endDate: ''
        }
      },
      querySearch(queryString, cb) {
        var employees = this.employees;
        var results = queryString ? employees.filter(this.createStateFilter(queryString)) : employees;
        cb(results);
      },
      createStateFilter(queryString) {
        return (state) => {
          return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      handleSelect(item) {
        this.detailResource.employeeId = item.id;
        this.detailResource.employeeName = item.value;
        this.detailResource.resourceLevel = item.titleLevel;
        //console.log(item);
      },
      showAddResourceView(){

      }
    }
  };
</script>
<style>
  .el-dialog__body {
    padding-top: 0px;
    padding-bottom: 0px;
  }

  .slide-fade-enter-active {
    transition: all .8s ease;
  }

  .slide-fade-leave-active {
    transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
  }

  .slide-fade-enter, .slide-fade-leave-to {
    transform: translateX(10px);
    opacity: 0;
  }
</style>
