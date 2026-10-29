import React, {useEffect, useState} from 'react';
import {getBpmnSpec, startBpmn} from "../../../api";
import InputField from "../../InputField/InputField";
import {useNavigate} from "react-router-dom";

const Index = ({uri}) => {
    const [specs, setSpecs] = useState({"dto": [], "variable": []});
    const [payload, setPayload] = useState({"body": {}, "params": {}});
    const [withVariable, setWithVariable] = useState(false);
    const [auth] = useState(JSON.parse(window.localStorage.getItem("token")));
    const [redirect, setRedirect] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchSpecs = async () => {
            try {
                const {data: {data}} = await getBpmnSpec(uri);
                return data;
            } catch (e) {
                return e;
            }
        }

        fetchSpecs().then(r => {setSpecs({...specs, dto: r.dto, variable: r.variable}); createPayload(r.dto, r.variable)});
    }, [uri, redirect]);

    const createPayload = (specs, variables) => {
        let s = {"body": {}, "params": {}};
        specs.forEach(spec => s = {...s, body: {...s.body, [spec.name]: ""}});
        s.body.user = {"id": auth.id};

        if (variables) {
            variables.forEach(vars => s = {...s, params: {...s.params, [vars.name]: ""}});
        }
        setPayload(s);
    }

    const handleShowVariable = () => setWithVariable(prevState => !prevState);

    const handleChangeVariable = (e) => setPayload({...payload, params: {...payload.params, [e.target.name]: e.target.value}});
    
    const handleChangePayload = (e) => {
        if (e.target.name === "category") {
            setPayload({...payload, body: {...payload.body, [e.target.name]: {"id": e.target.value}}});
        } else {
            setPayload({...payload, body: {...payload.body, [e.target.name]: e.target.value}});
        }
    }

    const handleSubmitForm = async (e) => {
        e.preventDefault();
        try {
            const {data} = await startBpmn(`ManajemenDokumenProyekAkhir?withVariable=${withVariable ? "true" : "false"}`, payload.body, withVariable ? {params: payload.params} : {});
            navigate("/pengajuan-proyek-akhir");
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <div className="max-w-[600px] w-full m-auto box-border">
            <div className="flex justify-between">
                <h1 className="font-bold text-lg">Form</h1>
                {
                    specs.variable.length && (<button onClick={handleShowVariable} className="p-2 bg-blue-400 text-white px-4 rounded">With Variable</button>)
                }
            </div>
            <div>
                {
                    withVariable && (
                        specs.variable.map((v, i) => (
                            <InputField key={i} cssClass="p-2 bg-gray-100" type={v.type} name={v.name} value={v.value} label={v.label} onChange={handleChangeVariable} />
                        ))
                    )
                }
            </div>
            <div className="box-border p-2">
                <form onSubmit={handleSubmitForm}>
                    {
                        specs.dto && (
                            specs.dto.map((spec, i) => <InputField modifiedField="user" modifiedValue={auth ? auth.namalengkap : ""} cssClass="p-2 bg-gray-100" type={spec.type} name={spec.name} label={spec.label} key={i} modified="user" onChange={handleChangePayload} />)
                        )
                    }
                    <button type="submit" className="w-full bg-blue-400 py-2 text-white font-bold rounded">Submit</button>
                </form>
            </div>
        </div>
    );
};

export default Index;
