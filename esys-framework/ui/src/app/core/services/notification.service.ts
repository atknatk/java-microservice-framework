import {Injectable} from "@angular/core";

declare const $ :any;

@Injectable()
export class NotificationService {

    info(message,title){
        this.show(message,title,'info');
    }

    warn(message,title){
        this.show(message,title,'warning');
    }

    succes(message,title){
        this.show(message,title,'success');
    }

    err(message,title?){
        if(title)
            this.show(message,title,'error');
        else
            this.show(message,"",'error');
    }


    private show(message,title,icon){
        $.toast({
            heading: title,
            text: message,
            position: 'top-right',
            loaderBg: '#fff',
            icon: icon,
            hideAfter: 3500,
            stack: 6
        })
    }

    showResult(result : any){
        if(result.message){
            $.toast({
                heading: result.title,
                text: result.message,
                position: 'top-right',
                loaderBg: '#fff',
                icon: result.messageType ? result.messageType : 'info',
                hideAfter: 3500,
                stack: 6
            })
        }

    }

}
