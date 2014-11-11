
function TimeOutNext(tmoutkind)
{
    var	WaitTime;
    var Msg;

    tmoutcnt++;

    switch( tmoutkind )
    {
        case '1':
            WaitTime = MINSEC * 4;
            break;

        case '2':
            WaitTime = MINSEC * 8;
            break;

        case '3':
            WaitTime = MINSEC * 18;
            break;
    }

    if( tmoutcnt == 2 )
    {
        WaitTime = MINSEC*5;
        Msg = "お客さまのブラウザーから一定時間アクセスがありませんでした。\n" +
            "時間を延長される場合は「ＯＫ(Yes)」ボタンを、このままログアウト\n" +
            "される場合は「キャンセル(No)」ボタンをクリックしてください";

        if ( true == confirm( Msg ))
        {
            clearTimeout( timerID );
            timerID = setTimeout( "TimeOutNext(SHORT)", WaitTime );
        }else{
            document.HOST.action="/TDGate2/gate/NBW000020";
            document.HOST.target="_self";
            document.HOST.submit();
        }
    }else if( tmoutcnt > 2 ){
        Msg = "延長時間を過ぎてもアクセスがありませんでしたので\n" +
            "ログアウトします";
        alert(Msg);
        document.HOST.action="/TDGate2/gate/NBW000020";
        document.HOST.target="_self";
        document.HOST.submit();
    }
}


function TimeOut3Next()
{
    var	WaitTime;
    var	Msg;
    tmoutcnt++;

    if( tmoutcnt == 2 )
    {
        WaitTime = MINSEC * 5;
        Msg = "お客さまのブラウザーから一定時間アクセスがありませんでした。\n" +
            "時間を延長される場合は「ＯＫ(Yes)」ボタンを、このまま終了される\n" +
            "場合は「キャンセル(No)」ボタンをクリックしてください";

        if ( true == confirm( Msg ))
        {
            clearTimeout( timerID );
            timerID = setTimeout( "TimeOut3Next()", WaitTime );
        }else{
            window.close();
        }
    }else if( tmoutcnt > 2 ){
        Msg = "延長時間を過ぎてもアクセスがありませんでしたので\n" +
            "ログアウトします";
        alert(Msg);
        document.HOST.action="/TDGate2/gate/NBW000020";
        document.HOST.target="_self";
        document.HOST.submit();
    }
}

function TimeOut4Next(tmoutkind)
{
    var	WaitTime;
    var Msg;

    tmoutcnt++;

    switch( tmoutkind )
    {
        case '1':
            WaitTime = MINSEC * 4;
            break;

        case '2':
            WaitTime = MINSEC * 8;
            break;

        case '3':
            WaitTime = MINSEC * 18;
            break;
    }

    if( tmoutcnt == 2 )
    {
        WaitTime = MINSEC*5;
        Msg = "お客さまのブラウザーから一定時間アクセスがありませんでした。\n" +
            "時間を延長される場合は「ＯＫ(Yes)」ボタンを、このままログアウト\n" +
            "される場合は「キャンセル(No)」ボタンをクリックしてください";

        if ( true == confirm( Msg ))
        {
            clearTimeout( timerID );
            timerID = setTimeout( "TimeOut4Next(SHORT)", WaitTime );
        }else{
            window.open("","logoutWin","width=800,height=600,toolbar=no,location=yes,directories=no,status=yes,menubar=no,resizable=yes,scrollbars=yes");
            document.HOST.action="/TDGate2/gate/NBW000020";
            document.HOST.target="logoutWin";
            document.HOST.submit();
        }
    }else if( tmoutcnt > 2 ){
        Msg = "延長時間を過ぎてもアクセスがありませんでしたので\n" +
            "ログアウトします";
        alert(Msg);
        window.open("","logoutWin","width=800,height=600,toolbar=no,location=yes,directories=no,status=yes,menubar=no,resizable=yes,scrollbars=yes");
        document.HOST.action="/TDGate2/gate/NBW000020";
        document.HOST.target="logoutWin";
        document.HOST.submit();
    }
}
