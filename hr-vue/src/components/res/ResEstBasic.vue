<template>
  <div>
    <el-container>
      <el-header style="padding: 0px;display:flex;justify-content:space-between;align-items: center">
        <div style="display: inline">
          <el-input
            placeholder="通过项目名搜索项目,记得回车哦..."
            clearable
            @change="keywordsChange"
            style="width: 300px;margin: 0px;padding: 0px;"
            size="mini"
            :disabled="advanceSearchViewVisible"
            @keyup.enter.native="searchProject"
            prefix-icon="el-icon-search"
            v-model="keywords">
          </el-input>
          <el-button type="primary" size="mini" style="margin-left: 5px" icon="el-icon-search" @click="searchResource">搜索
          </el-button>
        </div>
        <div style="margin-left: 5px;margin-right: 20px;display: inline">
          <el-upload
            :show-file-list="false"
            accept="application/vnd.ms-excel"
            action="/resource/est/import"
            :on-success="fileUploadSuccess"
            :on-error="fileUploadError" :disabled="fileUploadBtnText=='正在导入'"
            :before-upload="beforeFileUpload" style="display: inline">
            <el-button size="mini" type="success" :loading="fileUploadBtnText=='正在导入'"><i class="fa fa-lg fa-level-up"
                                                                                          style="margin-right: 5px"></i>{{fileUploadBtnText}}
            </el-button>
          </el-upload>
          <el-button type="success" size="mini" @click="exportResource"><i class="fa fa-lg fa-level-down"
                                                                       style="margin-right: 5px"></i>导出数据
          </el-button>
          <el-button type="primary" size="mini" icon="el-icon-plus"
                     @click="showAddResourceView">
            添加资源
          </el-button>
        </div>
      </el-header>
      <el-main style="padding-left: 0px;padding-top: 0px">
        <div>
          <el-table
            :data="resources"
            v-loading="tableLoading"
            border
            stripe
            @selection-change="handleSelectionChange"
            size="mini"
            style="width: 100%">
            <el-table-column
              type="selection"
              align="left"
              width="30">
            </el-table-column>
            <el-table-column
              prop="month"
              align="left"
              fixed
              label="资源月"
              width="120">
            </el-table-column>
            <el-table-column
              prop="projectName"
              align="left"
              fixed
              label="项目名"
              width="120">
            </el-table-column>
            <el-table-column
              prop="projectCode"
              width="120"
              align="left"
              label="项目编号">
            </el-table-column>
            <el-table-column
              prop="jpProjectCode"
              width="120"
              align="left"
              label="日本项目编号">
            </el-table-column>
            <el-table-column
              prop="members"
              width="120"
              align="left"
              label="预计人数">
            </el-table-column>
            <el-table-column
              prop="resourceTotal"
              align="left"
              label="预计资源数"
              width="140">
            </el-table-column>
            <el-table-column
              width="150"
              align="left"
              label="状态">
               <template slot-scope="scope">
                <span v-if="scope.row.status === '0'">未锁定</span>
                <span v-if="scope.row.status === '1'">已锁定</span>
              </template>
            </el-table-column>
            <el-table-column
              width="95"
              align="left"
              label="创建日期">
              <template slot-scope="scope">{{ scope.row.createdDate | formatDate}}</template>
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="105">
              <template slot-scope="scope">
                <el-button @click="showEditPesourceView(scope.row)" style="padding: 3px 4px 3px 4px;margin: 2px"
                           size="small">编辑
                </el-button>
                <el-button @click="lockResource(scope.row)" style="padding: 3px 4px 3px 4px;margin: 2px"
                           size="small">锁定
                </el-button>
                <el-button @click="releaseLockResource(scope.row)" style="padding: 3px 4px 3px 4px;margin: 2px"
                           size="small">解除锁定
                </el-button>
                <el-button type="danger" style="padding: 3px 4px 3px 4px;margin: 2px" size="small"
                           @click="deleteResource(scope.row)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="display: flex;justify-content: space-between;margin: 2px">
            <el-button type="danger" size="mini" v-if="resources.length>0" :disabled="multipleSelection.length==0"
                       @click="deleteManyResource">批量删除
            </el-button>
            <el-pagination
              background
              :page-size="10"
              :current-page="currentPage"
              @current-change="currentChange"
              layout="prev, pager, next"
              :total="totalCount">
            </el-pagination>
          </div>
        </div>
      </el-main>
    </el-container>
    <el-form :model="resource" :rules="rules" ref="addResourceForm" style="margin: 0px;padding: 0px;">
      <div style="text-align: left">
        <el-dialog
          :title="dialogTitle"
          style="padding: 0px;"
          :close-on-click-modal="false"
          :visible.sync="dialogVisible"
          width="70%">
          <el-row>
            <el-col :span="6">
              <div>
                <el-form-item label="资源月:" prop="month">
                  <el-date-picker
                    v-model="resource.month"
                    size="mini"
                    style="width: 130px"
                    type="month"
                    value-format="yyyy-MM"
                    placeholder="入职日期">
                  </el-date-picker>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="6">
              <div>
                <el-form-item label="项目:" prop="projectId">
                  <el-select v-model="resource.projectId" style="width: 150px" size="mini" placeholder="请选择项目">
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
          </el-row>
          
          <span slot="footer" class="dialog-footer">
    <el-button size="mini" @click="cancelEidt">取 消</el-button>
    <el-button size="mini" type="primary" @click="addResource('addResourceForm')">确 定</el-button>
  </span>
        </el-dialog>
      </div>
    </el-form>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        resources: [],
        keywords: '',
        fileUploadBtnText: '导入数据',
        beginDateScope: '',
        faangledoubleup: 'fa-angle-double-up',
        faangledoubledown: 'fa-angle-double-down',
        dialogTitle: '',
        multipleSelection: [],
        depTextColor: '#c0c4cc',
        projectStatus: [{id: 0, name: '未开始'}, {id: 1, name: '进行中'}, {id: 6, name: '已结束'}],
       
        totalCount: -1,
        currentPage: 1,
       
        deps: [],
        defaultProps: {
          label: 'name',
          isLeaf: 'leaf',
          children: 'children'
        },
        dialogVisible: false,
        tableLoading: false,
        advanceSearchViewVisible: false,
        showOrHidePop: false,
        showOrHidePop2: false,
        projects: [],
        resource: {
          //id: '',
          month: '',
          projectName: '',
          projectCode: '',
          jpProjectCode: '',
          members: '',
          resourceTotal: '',
          status: ''
        },
        rules: {
          month: [{required: true, message: '必填:资源月', trigger: 'blur'}],
          projectId: [{required: true, message: '必填:项目', trigger: 'blur'}]
        }
      };
    },
    mounted: function () {
      this.initData();
      this.loadResources();
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
        this.loadResources();
      },
      showAdvanceSearchView(){
        this.advanceSearchViewVisible = !this.advanceSearchViewVisible;
        this.keywords = '';
        if (!this.advanceSearchViewVisible) {
          this.emptyResourceData();
          this.beginDateScope = '';
          this.loadResources();
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
            _this.loadResources();
          }
        })
      },
      keywordsChange(val){
        if (val == '') {
          this.loadResources();
        }
      },
      searchResource(){
        this.loadResources();
      },
      currentChange(currentChange){
        this.currentPage = currentChange;
        this.loadPesources();
      },
      loadResources(){
        var _this = this;
        this.tableLoading = true;
        this.getRequest("/resource/est/list?page=" + this.currentPage + "&size=10&keywords=" + this.keywords).then(resp=> {
          this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.resources = data.resources;
            _this.totalCount = data.count;
//            _this.emptyResourceData();
          }
        })
      },
      addResource(formName){
        var _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.project.id) {
              //更新
              this.tableLoading = true;
              this.putRequest("/resource/est/update", this.project).then(resp=> {
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
              this.postRequest("/resource/est/add", this.project).then(resp=> {
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
        this.getRequest("/project/basic/basicdata").then(resp=> {
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.nations = data.nations;
            _this.politics = data.politics;
            _this.deps = data.deps;
            _this.positions = data.positions;
            _this.joblevels = data.joblevels;
          }
        })
      },
      showEditResourceView(row){
        console.log(row)
        this.dialogTitle = "编辑项目";
        this.project = row;
        this.project.startDate = this.formatDate(row.startDate);
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
      showAddResourceView(){
        var _this = this;
        _this.$router.push("/res/ResEstAdd");
      },
      showEditPesourceView(row){
        var _this = this;
        _this.$router.push({name: '资源详细', params:{editResourceId: row.resourceId} });
      },
      emptyResourceData(){
        this.project = {
          month: '',
          projectName: '',
          projectCode: '',
          estCount: '',
          estResCount: '',
          status: ''
        }
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
