import BpmnJs from "bpmn-js";

var $ = require( 'jquery' );

export const RenderXml = async (idWrapper, btnReset, btnZoomIn, btnZoomOut, xml, tasks, listTasks) => {
    const bpmnJs = new BpmnJs({
        container: idWrapper,
        keyboard: {
            bindTo: window
        }
    });

    const getCount = (task) => {
        if (task === 'submitPraProposal' || task === 'submitLaporanMbkm'){
            return `<div class="block" style="background-color: #bd2130;padding: 3px; border-radius: 15px;"><b><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></b></div>`
        }
        else if (task==='reviewKaprodi'){
            return `<div class="block" style="background-color: #bd2130;padding: 3px; border-radius: 15px;"><b><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></b></div>`
        } else if (task==='reviewKoordinatorPA'){
            return `<div class="block" style="background-color: #dc3545;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='submitMbkmKaprodi'){
            return `<div class="block" style="background-color: #e83e8c;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='reviewTimKBK'){
            return `<div class="block" style="background-color: #d39e00;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='submitKoordinatorPA'){
            return `<div class="block" style="background-color: #c69500;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='submitKaprodi'){
            return `<div class="block" style="background-color: #fd7e14;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='revisiJudul'){
            return `<div class="block" style="background-color: #ffdf7e;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='submitProposal'){
            return `<div class="block" style="background-color: #ffe8a1;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='revisiProposal'){
            return `<div class="block" style="background-color: #80bdff;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='reviewProposal'){
            return `<div class="block" style="background-color: #007bff;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='daftarSeminar'){
            return `<div class="block" style="background-color: #0069d9;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='aturJadwalSeminar'){
            return `<div class="block" style="background-color: #005cbf;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='inputHasilSeminar'){
            return `<div class="block" style="background-color: #10707f;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='submitLaporan'){
            return `<div class="block" style="background-color: #0c5460;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='revisiLaporan'){
            return `<div class="block" style="background-color: #20c997;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='reviewPembimbing'){
            return `<div class="block" style="background-color: #218838;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        } else if (task==='daftarSidang'){
            return `<div class="block" style="background-color: #1c7430;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        }  else if (task==='aturJadwalSidang'){
            return `<div class="block" style="background-color: #155724;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        }  else if (task==='inputHasilSidang'){
            return `<div class="block" style="background-color: #28a745;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        }
        else {
            return `<div class="block" style="background-color: lime;padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${tasks.tasks[task]}</span></div>`
        }
    }
    try {
        await bpmnJs.importXML(xml);
        const canvas = bpmnJs.get("canvas");
        const overlays = bpmnJs.get("overlays");
        let zlevel = 1, zstep = 0.2;

        function reset(){
            canvas.zoom("fit-viewport");
        }

        function zoomIn(){
            zlevel = Math.max( zlevel - zstep, zstep );
            canvas.zoom( zlevel, 'auto' );
        }

        function zoomOut(){
            zlevel = Math.min( zlevel + zstep, 7 );
            canvas.zoom( zlevel, 'auto' );
        }

        const elementRegistry = bpmnJs.get('elementRegistry');
        for (const task in tasks.tasks) {
            let count = 0;
            if (listTasks) {
                const res = listTasks.filter(ls => ls.tasks[task]);
                count = res.length;
            }

            overlays.add(task, {
                position: {
                    bottom: 28,
                    left: 4
                },
                html: getCount(task)
            })
        }

        let countSub = 0;
        for (const task in tasks.subprocess.BimbinganProposalProyekAkhir) {
            let val = tasks.subprocess.BimbinganProposalProyekAkhir[task];
            countSub+=val;
        }

        overlays.add("bimbinganPraSeminar", {
            position: {
                bottom: 28,
                left: 4
            },
            html: `<div class="block" style="background-color: #007bff; padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${countSub}</span></div>`
        })

        let countSub2 = 0;
        for (const task in tasks.subprocess.BimbinganProyekAkhir) {
            const val = tasks.subprocess.BimbinganProyekAkhir[task];
            countSub2+=val;
        }

        overlays.add("bimbinganPraSidang", {
            position: {
                bottom: 28,
                left: 4
            },
            html: `<div class="block" style="background-color: #007bff; padding: 3px; border-radius: 15px;"><span class="rounded-full bg-blue-400 w-[25px] h-[25px] text-[1rem] flex justify-center align-center font-bold" style="color: white">${countSub2}</span></div>`
        })

        canvas.zoom("fit-viewport");
        $(btnReset).click(() => reset());
        $(btnZoomIn).click(() => zoomIn());
        $(btnZoomOut).click(() => zoomOut());


    } catch (e) {
        console.log(e);
    }
}
