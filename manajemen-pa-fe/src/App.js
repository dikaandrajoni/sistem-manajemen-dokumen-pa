import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import Dashboard from "./pages/dashboard";
import Register from "./pages/register";
import NotFound from "./pages/notfound";
import FileUploadPage from "./pages/fileupload";
import DrawSignature from "./pages/signature/DrawSignature";
import ListUser from "./pages/user/ListUser";
import AddUser from "./pages/user/AddUser";
import ListRole from "./pages/role/ListRole";
import AddRole from "./pages/role/AddRole";
import PengajuanProyekAkhir from "./pages/pengajuanproyekakhir/PengajuanProyekAkhir";
import DataMahasiswa from "./pages/mahasiswa/DataMahasiswa";
import DataDosen from "./pages/dosen/DataDosen";
import ReviewProyekAkhir from "./pages/review/ReviewProyekAkhir";
import DataProgramStudi from "./pages/programstudi/DataProgramStudi";
import UbahPassword from "./pages/user/UbahPassword";
import ListHasilReview from "./pages/review/ListHasilReview";
import Home from "./pages/home/Home";
import Login from "./pages/login";

class App extends Component {

    render() {
        return (
            <div className="App" >
                <Router>
                    <Switch>
                        <Route exact path='/' component={Home} />
                        <Route path='/login' component={Login} />
                        <Route path='/dashboard' component={Dashboard} />
                        <Route path='/dosen' component={DataDosen}/>
                        <Route path='/mahasiswa' component={DataMahasiswa}/>
                        <Route path='/program-studi' component={DataProgramStudi}/>
                        <Route path='/register' component={Register} />
                        <Route path='/fileupload/' component={FileUploadPage} />
                        <Route path='/pengajuan-proyek-akhir' component={PengajuanProyekAkhir} />
                        <Route path='/review-proyek-akhir' component={ReviewProyekAkhir} />
                        <Route path='/hasil-review' component={ListHasilReview} />
                        <Route path='/signature' component={DrawSignature} />
                        <Route path='/users' component={ListUser} />
                        <Route path='/add-user' component={AddUser} />
                        <Route path='/ubah-password' component={UbahPassword} />
                        <Route path='/add-role' component={AddRole} />
                        <Route path='/roles' component={ListRole} />
                        <Route path='*' component={NotFound} />
                    </Switch>
                </Router>
            </div>
        );
    }
}

export default App;
