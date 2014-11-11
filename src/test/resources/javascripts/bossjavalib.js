var	EMPTY		= "1";
var	ACCEPT		= "2";
var	WRONG		= "3";
var	SELECT		= "4";
var	FREE		= "5";

var	SHORT		= "1";
var	MIDDLE		= "2";
var	LONG 		= "3";

var	TRUE		= eval( 1 );
var	FALSE		= eval( 0 );

function IsEmpty(obj , name)
{
    var wRet;

    spacetrim(0,obj);

    if((obj.value == null) || (obj.value == "")){
        ErrMsg(obj , EMPTY , name);
        wRet = false;
    } else {
        wRet = true;
    }

    return( wRet );
}

function IsEmptySub( flg, obj , name)
{
    spacetrim(0,obj);

    if( flg == 0 )
    {
        if( obj.value.length <= 0 ) return( 0 );
    }else{
        if( !IsEmpty(obj , name )) return( -1 );
    }

    return( 1 );
}

function IsSingle( flg, obj , name)
{
    var i;
    var	wRet;

    spacetrim(0,obj);
    wRet = IsEmptySub( flg, obj , name );

    if( wRet < 0 )
    {
        return( FALSE );
    } else if( wRet == 0 )
    {
        return( TRUE );
    }

    for( i=0 ; i< obj.value.length ; i++ )
    {
        if(	escape( obj.value.charAt(i) ).charAt( 1 ) >= "8" &&
            escape( obj.value.charAt(i) ).charAt( 1 ) <= "F" )
        {
            ErrMsg(obj , ACCEPT , name + "を半角文字");
            return( FALSE );
        } else if( escape( obj.value.charAt(i) ).charAt( 1 ) == "u" ){
            ErrMsg(obj , ACCEPT , name + "を半角文字");
            return( FALSE );
        }

    }

    return( TRUE );
}

function IsDouble( flg, obj , name )
{
    var	wIdx;
    var	wRet;

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    } else if( wRet == 0 ){
        return( TRUE );
    }

    for( wIdx = 0, wRet = TRUE; wIdx < obj.value.length; wIdx++ )
    {
        if( escape( obj.value.charAt(wIdx) ).length >= 4 )
        {
            if(	escape( obj.value.charAt(wIdx) ) >= "%uFF61" &&
                escape( obj.value.charAt(wIdx) ) <= "%uFF9F" ) wRet = FALSE;
        }else{
            wRet = FALSE;
        }

        if( wRet == FALSE )
        {
            ErrMsg(obj , ACCEPT , name + "を全角文字");
            wRet = FALSE;
            break;
        }
    }

    return( wRet );
}

function IsDoubleKana( flg, obj , name )
{

    var KanaTbl = "　アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン" +
        "ガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポァィゥェォヵヶッャュョヮヴヰヱー";

    var i;
    var j;
    var wStrHit;
    var	wRet;

    spacetrim(0,obj);

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    } else if( wRet == 0 ) {
        return( TRUE );
    }

    for( i=0 ; i<obj.value.length ; i++ )
    {
        for( j=0 , wStrHit=false ; j<KanaTbl.length ; j++ )
        {
            if( obj.value.charAt(i) == KanaTbl.charAt(j) )
            {
                wStrHit = true;
                break;
            }
        }

        if ( wStrHit != true )
        {
            ErrMsg(obj , ACCEPT , name + "を全角カタカナ");
            return( FALSE );
        }
    }

    return( TRUE );
}

function IsAlpha( flg, obj , name )
{
    var i;
    var j;
    var wStrHit;

    spacetrim(0,obj);

    var	wRet;
    wRet = IsEmptySub( flg, obj , name );

    if( wRet < 0 )
    {
        return( FALSE );
    } else if( wRet == 0 ) {
        return( TRUE );
    }

    for( i=0 ; i<obj.value.length ; i++ )
    {
        if(( obj.value.charAt(i) >= "A" && obj.value.charAt(i) <= "Z" ) ||
            ( obj.value.charAt(i) >= "a" && obj.value.charAt(i) <= "z" ) )
        {
            continue;
        } else {
            ErrMsg(obj , ACCEPT , name + "を半角英字");
            return( FALSE );
        }
    }

    return( TRUE );
}

function IsNumZeroPress( flg, obj , name, keta )
{
    var wStrHit;
    var	wRet;
    var i;

    spacetrim(0,obj);

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        zeroPress( obj, keta );
        return( TRUE );
    }

    for( i=0 ; i<obj.value.length ; i++ )
    {
        if(( obj.value.charAt(i) >= "0" && obj.value.charAt(i) <= "9" ) ||
            obj.value.charAt(i) == "." || obj.value.charAt(i) == ","  ||
            obj.value.charAt(i) == "-" )
        {
            continue;
        }else{
            ErrMsg(obj , ACCEPT , name + "を半角数字");
            return( FALSE );
        }
    }

    zeroPress( obj, keta );
    return( TRUE );
}

function zeroPress( obj, keta )
{
    var zero;
    var i;

    zero = "";
    for( i=obj.value.length ; i<keta; i++ )
    {
        zero += "0";
    }

    obj.value = zero + obj.value;
    return( TRUE );
}

function IsNum( flg, obj , name )
{
    var wStrHit;
    var	wRet;
    var i;

    spacetrim(0,obj);

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    for( i=0 ; i<obj.value.length ; i++ )
    {
        if(( obj.value.charAt(i) >= "0" && obj.value.charAt(i) <= "9" ) ||
            obj.value.charAt(i) == "." || obj.value.charAt(i) == ","  ||
            obj.value.charAt(i) == "-" )
        {
            continue;
        }else{
            ErrMsg(obj , ACCEPT , name + "を半角数字");
            return( FALSE );
        }
    }

    return( TRUE );
}

function	IsDateChk( yyyy, mm, dd )
{
    yyyy = spacetrim2(0,yyyy);
    if( IsNAN(yyyy) == true ) return( 1 );

    mm = spacetrim2(0,mm);
    if( IsNAN(mm) == true || eval(mm) < 1 || 12 < eval(mm)) return( 2 );

    dd = spacetrim2(0,dd);
    if( IsNAN(dd) == true || eval(dd) < 1 || 31 < eval(dd)) return( 3 );

    var wkDate	= new Date(eval(yyyy) + "/" + eval(mm) + "/" + eval(dd));
    var wkYear	= wkDate.getFullYear();
    var wkMonth	= wkDate.getMonth();
    var wkDay	= wkDate.getDate();
    wkMonth		= wkMonth + 1;

    if ( eval(yyyy) == eval(wkYear) && eval(wkMonth) == eval(mm) && eval(dd) == eval(wkDay) )
    {
        return( 0 );
    }else{
        return( 3 );
    }
}

function	IsDateChk2( ck, yyyy, mm, dd, name )
{
    var  wRetUp;
    var  UpCnt;

    if( yyyy.value.length > 0 || mm.value.length > 0 || dd.value.length > 0 ) ck = 1;

    UpCnt = 0;

    wRetUp = IsEmptySub( ck, yyyy, name + "(年)" );
    if( wRetUp < 0 ) return( FALSE );
    UpCnt = UpCnt + wRetUp;

    wRetUp = IsEmptySub( ck, mm, name + "(月)" );
    if( wRetUp < 0 ) return( FALSE );

    UpCnt = UpCnt + wRetUp;
    wRetUp = IsEmptySub( ck, dd, name + "(日)" );
    if( wRetUp < 0 ) return( FALSE );

    UpCnt = UpCnt + wRetUp;
    if ( UpCnt == 0 ) return(TRUE);

    if( IsNAN(yyyy.value) == true )
    {
        ErrMsg( yyyy, WRONG, name + "(年)");
        return( FALSE );
    }

    if( IsNAN(mm.value) == true || eval(mm.value) < 1 || 12 < eval(mm.value))
    {
        ErrMsg( mm, WRONG, name + "(月)");
        return( FALSE );
    }

    if( IsNAN(dd.value) == true || eval(dd.value) < 1 || 31 < eval(dd.value))
    {
        ErrMsg( dd, WRONG, name + "(日)");
        return( FALSE );
    }

    var wkDate	= new Date(eval(yyyy.value) + "/" + eval(mm.value) + "/" + eval(dd.value));
    var wkYear	= wkDate.getFullYear();
    var wkMonth	= wkDate.getMonth();
    var wkDay	= wkDate.getDate();
    wkMonth		= wkMonth + 1;

    if ( eval(yyyy.value) == eval(wkYear)  &&
        eval(mm.value)   == eval(wkMonth) &&
        eval(dd.value)   == eval(wkDay) )
    {
        return( TRUE );
    }else{
        ErrMsg( dd, WRONG, name );
        return( FALSE );
    }
}

function getPopName(id)
{
    var wRet;
    id.value = id.value.replace(/(\s*)(\S*)(\s*)/,"$2");
    if(id.value == "NBP019902"){
        wRet = "bankname";
    } else {
        wRet = "";
    }
    return( wRet );
}

function IsNumOnly( flg, obj, name )
{
    var wStrHit;
    var	wRet;
    var i;

    spacetrim(0,obj);
    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    for( i=0 ; i<obj.value.length ; i++ )
    {
        if(( obj.value.charAt(i) >= "0" && obj.value.charAt(i) <= "9" ))
        {
            continue;
        }else{
            ErrMsg(obj , ACCEPT , name + "を半角数字");
            return( FALSE );
        }
    }

    return( TRUE );
}

function	IsZip( flg, objUp, objLow, name )
{
    var	wRetUp;
    var	wRetLow;

    wRetUp = IsEmptySub( flg, objUp, name );
    if( wRetUp < 0 ) return( FALSE );

    wRetLow = IsEmptySub( flg, objLow, name );
    if( wRetLow < 0 ) return( FALSE );

    if( wRetUp == 0 && wRetLow == 0 ) return( TRUE );

    if( objUp.value.length < 3 )
    {
        ErrMsg( objUp, ACCEPT, name + "を3ケタ" );
        return( FALSE );
    }

    if( objLow.value.length < 4 )
    {
        ErrMsg( objLow, ACCEPT, name + "を4ケタ" );
        return( FALSE );
    }

    if (IsNAN( objUp.value ) == true )
    {
        ErrMsg(objUp , ACCEPT , name + "を半角数字");
        return( FALSE );
    }

    if (IsNAN( objLow.value ) == true )
    {
        ErrMsg(objLow , ACCEPT , name + "を半角数字");
        return( FALSE );
    }

    return( TRUE );
}

function IsNAN( obj )
{
    var nums = "0123456789";

    for( var i=0 ; i<obj.length ; i++ )
    {
        thisChar = obj.substring(i,i+1);
        if (nums.indexOf(thisChar) < 0) return( TRUE );
    }

    return( FALSE );
}

function	IsPlusZero( objCheck, Name )
{
    if( !IsEmpty(objCheck , Name)) return( FALSE );

    if (isNaN( objCheck.value ) == true )
    {
        ErrMsg( objCheck, WRONG, Name )
        return( FALSE );
    }

    if( 0 <= eval( objCheck.value ))
    {
        return( TRUE );
    }else{
        ErrMsg( objCheck, WRONG, Name )
        return( FALSE );
    }
}

function	IsPlus( objCheck, Name )
{

    if( !IsEmpty(objCheck , Name)) return( FALSE );

    if (isNaN( objCheck.value ) == true )
    {
        ErrMsg( objCheck, WRONG, Name )
        return( FALSE );
    }

    if( 0 < eval( objCheck.value ))
    {
        return( TRUE );
    }else{
        ErrMsg( objCheck, WRONG, Name )
        return( FALSE );
    }
}

function	IsPswd( objCheck, Name )
{
    var	bRet = FALSE;
    var wIdx;

    if( !IsEmpty(objCheck , Name)) return( FALSE );

    for( wIdx = 0, bRet = FALSE; wIdx < objCheck.value.length; wIdx++ )
    {
        if( 0 != objCheck.value.charAt( wIdx ))
        {
            bRet = TRUE;
            break;
        }
    }

    if( bRet == FALSE ) ErrMsg( objCheck, WRONG, Name );

    return( bRet );
}

function	IsPswdW( objCheck1, objCheck2, Name )
{
    var	bRet;
    var	wIdx;

    if( !IsEmpty(objCheck1 , Name ) || !IsEmpty(objCheck2 , Name) ) return( FALSE );

    if( objCheck1.value.length != objCheck2.value.length )
    {
        ErrMsg( objCheck2, WRONG, Name );
        return( FALSE );
    }

    for( wIdx = 0, bRet = TRUE; wIdx < objCheck1.value.length; wIdx++ )
    {
        if( objCheck2.value.charAt( wIdx ) != objCheck1.value.charAt( wIdx ))
        {
            bRet = FALSE;
            ErrMsg( objCheck2, WRONG, Name );
            break;
        }
    }

    return( bRet );
}

function	RadioChk( objCheck, Name )
{
    var	bRet;
    var	wIdx;

    if ( objCheck.length > 0 )
    {
        for( wIdx = 0, bRet = FALSE; wIdx < objCheck.length; wIdx++ )
        {
            if( objCheck[wIdx].checked == true )
            {
                bRet = TRUE;
                break;
            }
        }
        if( bRet == FALSE ) ErrMsg( objCheck[0], SELECT, Name );
    } else {
        bRet = objCheck.checked;
        if( bRet == FALSE ) ErrMsg( objCheck, SELECT, Name );
    }

    return( bRet );
}

function	RadioChk2( objCheck, Name )
{
    var	bRet;
    var	wIdx;

    if ( objCheck.length > 0 )
    {
        for( wIdx = 0, bRet = FALSE; wIdx < objCheck.length; wIdx++ )
        {
            if( objCheck[wIdx].checked == true )
            {
                bRet = TRUE;
                break;
            }
        }
        if( bRet == FALSE ) {
            for( wIdx = 0; wIdx < objCheck.length; wIdx++ )
            {
                if( !objCheck[wIdx].disabled )
                {
                    ErrMsg( objCheck[wIdx], SELECT, Name );
                    return;
                }
            }
        }
    } else {
        bRet = objCheck.checked;
        if( bRet == FALSE && !objCheck.disabled) ErrMsg( objCheck, SELECT, Name );
    }

    return( bRet );
}

function	ChkBoxChk( objCheck, Name )
{
    var	bRet;
    var	wIdx;

    bRet = FALSE;

    if( objCheck.length > 1 )
    {
        for( wIdx = 0 ; wIdx < objCheck.length; wIdx++ )
        {
            if( objCheck[ wIdx ].checked == true )
            {
                bRet = TRUE;
                break;
            }
        }
    }else{
        if( objCheck.checked == true ) bRet = TRUE;
    }

    if( bRet == FALSE )
    {
        if( objCheck.length > 1 )
        {
            ErrMsg( objCheck[0], SELECT, Name );
        }else{
            ErrMsg( objCheck, SELECT, Name );
        }
    }

    return( bRet );
}

function	PullDownChk( objCheck, Name )
{
    if( objCheck.options[ objCheck.selectedIndex ].value == "" )
    {
        ErrMsg( objCheck, SELECT, Name );
        return( FALSE );
    }else{
        return( TRUE );
    }
}

function	Wa2Sei( flg, yy )
{
    var	wkYear = -1;

    if (isNaN( yy ) == true ) return( wkYear );

    if( flg == 0 )
    {
        wkYear = 1866;
    }else if( flg == 1 ){
        wkYear = 1911;
    }else if( flg == 2 ){
        wkYear = 1925;
    }else if( flg == 3 ){
        wkYear = 1988;
    }else{
        return( wkYear );
    }

    wkYear += eval( yy );
    return( wkYear );
}

var	timerID = 10;
var	tmoutcnt;
var MINSEC	= 60000;

function TimeOut(tmoutkind)
{
    var	WaitTime;
    var Msg;

    tmoutcnt=1;

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

    clearTimeout( timerID );
    timerID = setTimeout( "TimeOutNext(SHORT)", WaitTime );
}

function TimeOut2(tmoutkind)
{
    var	WaitTime;
    var Msg;

    tmoutcnt=1;

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

    clearTimeout( timerID );
    timerID = setTimeout( "TimeOut2Next(SHORT)", WaitTime );
}

function TimeOut3()
{
    var	WaitTime;
    var	Msg;

    tmoutcnt=1;

    WaitTime = MINSEC * 25;

    clearTimeout( timerID );
    timerID = setTimeout( "TimeOut3Next()", WaitTime );
}

function TimeOut4(tmoutkind)
{
    var	WaitTime;
    var Msg;

    tmoutcnt=1;

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

    clearTimeout( timerID );
    timerID = setTimeout( "TimeOut4Next(SHORT)", WaitTime );
}

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

function TimeOut2Next(tmoutkind)
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
            "時間を延長される場合は「ＯＫ(Yes)」ボタンを、このまま終了される\n" +
            "場合は「キャンセル(No)」ボタンをクリックしてください";

        if ( true == confirm( Msg ))
        {
            clearTimeout( timerID );
            timerID = setTimeout( "TimeOut2Next(SHORT)", WaitTime );
        }else{
            window.close();
        }
    }else if( tmoutcnt > 2 ){
        Msg = "延長時間を過ぎてもアクセスがありませんでしたので\n" +
            "終了します";
        alert(Msg);
        window.close();
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

function	ShowNewPage( URL, Style)
{
    window.open( URL, "", Style );
}

function	ErrMsg( objMsg, fnc, msgExt )
{
    var strMessage;
    strMessage = "";

    switch ( fnc )
    {
        case "1":
            strMessage = msgExt + "を入力してください。";
            break ;

        case "2":
            strMessage = msgExt + "で入力してください。";
            break ;

        case "3":
            strMessage = msgExt + "を正しく入力してください。";
            break ;

        case "4":
            strMessage = msgExt + "を選択してください。";
            break ;

        case "5":
            strMessage = msgExt;
            break ;
    }

    alert( strMessage );
    objMsg.focus();
}

function	IsTel( flg, objHi, objMid, objLow, name )
{
    var	strMessage;
    var	wRetHi;
    var	wRetMid;
    var	wRetLow;
    var	i;

    wRetHi = IsEmptySub( flg, objHi, name );
    if( wRetHi < 0 ) return( 0 );

    wRetMid = IsEmptySub( flg, objMid, name );
    if( wRetMid < 0 ) return( 0 );

    wRetLow = IsEmptySub( flg, objLow, name );
    if( wRetLow < 0 ) return( 0 );

    if( wRetHi == 1 || wRetMid == 1 || wRetLow == 1 ) {

        if( wRetHi == 0 )
        {
            ErrMsg( objHi, EMPTY, name );
            return( 0 );
        }

        if( wRetMid == 0 )
        {
            ErrMsg( objMid, EMPTY, name );
            return( 0 );
        }

        if( wRetLow == 0 )
        {
            ErrMsg( objLow, EMPTY, name );
            return( 0 );
        }
    }

    if( !IsNum( flg, objHi, name ) || !IsNum( flg, objMid, name ) || !IsNum( flg, objLow, name )) return( 0 );

    for( i=0 ; i<objHi.value.length ; i++ )
    {
        if( objHi.value.charAt(i) == "." || objHi.value.charAt(i) == "," || objHi.value.charAt(i) == "-" )
        {
            ErrMsg(objHi , ACCEPT , name + "を半角数字");
            return( 0 );
        }
    }

    for( i=0 ; i<objMid.value.length ; i++ )
    {
        if( objMid.value.charAt(i) == "." || objMid.value.charAt(i) == "," || objMid.value.charAt(i) == "-" )
        {
            ErrMsg(objMid , ACCEPT , name + "を半角数字");
            return( 0 );
        }
    }

    for( i=0 ; i<objLow.value.length ; i++ )
    {
        if( objLow.value.charAt(i) == "." || objLow.value.charAt(i) == "," || objLow.value.charAt(i) == "-" )
        {
            ErrMsg(objLow , ACCEPT , name + "を半角数字");
            return( 0 );
        }
    }

    return( 1 );
}

function	IsExists( frm, name )
{
    var   elmCk = 0;
    var   i;

    for ( i = 0; i < frm.length; i++ )
    {
        var   Elm = frm.elements[i];
        if ( Elm.name == name ) elmCk = 1;
    }

    if ( elmCk == 0) return false;
    return true;
}

function IgnoreClick(frm)
{
    var i,n;

    n=frm.elements.length;

    for(i=0;i<n;i++) {
        var e=frm.elements[i];

        if(e.type=="radio" ) {
            if (e.defaultChecked) e.checked=true;
            else e.checked=false;
        }

        if(e.type=="checkbox" ) {
            if (e.defaultChecked) e.checked=true;
            else e.checked=false;
        }
    }
}

function IsAikotoba( flg, obj , name )
{
    var KanaTbl = "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン" +
        "ガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポァィゥェォヵヶッャュョヮヴヰヱー" +
        "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん" +
        "がぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽぁぃぅぇぉっゃゅょゎゐゑ";

    var i;
    var j;
    var wStrHit;

    spacetrim(0,obj);

    var	wRet;
    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    for( i=0 ; i<obj.value.length ; i++ )
    {
        for( j=0 , wStrHit=false ; j<KanaTbl.length ; j++ )
        {
            if( obj.value.charAt(i) == KanaTbl.charAt(j) )
            {
                wStrHit = true;
                break;
            }
        }

        if ( wStrHit != true )
        {
            ErrMsg(obj , ACCEPT , name + "をひらがな、または全角カタカナ");
            return( FALSE );
        }
    }
    return( TRUE );
}

function IsKingaku(flg,obj,name,ch)
{
    var	zcnt,dcnt,ccnt,mcnt,pcnt;
    var n,i;

    zcnt=0;dcnt=0;ccnt=0;mcnt=0;

    spacetrim(0,obj);

    n=obj.value.length;
    if(flg==1 && n==0) {
        ErrMsg(obj , EMPTY , name);
        return(FALSE);
    }

    if(n==0) return true;

    zcnt=CalcCharCnt(obj,"0",0);
    dcnt=CalcCharCnt(obj,".",0);
    ccnt=CalcCharCnt(obj,",",0);
    mcnt=CalcCharCnt(obj,"-",0);

    if(dcnt) {
        ErrMsg(obj , ACCEPT , name + "を小数のない半角数字");
        return FALSE;
    }

    if(zcnt==n) {
        obj.value="0";
        return TRUE;
    }

    if(mcnt) {
        if(mcnt>1) {
            ErrMsg(obj , ACCEPT , name + "を半角数字");
            return FALSE;
        }

        if(obj.value.charAt(0) != "-") {
            ErrMsg(obj , ACCEPT , name + "を半角数字");
            return FALSE;
        }
    }

    if(mcnt) pcnt=CalcCharCnt(obj,"0","-");
    else  pcnt=CalcCharCnt(obj,"0",1);

    if(pcnt) {
        if(mcnt) {
            if(pcnt== obj.value.length-1) obj.value="-0";
            else obj.value="-"+obj.value.substring(pcnt+1,n);
        } else {
            obj.value=obj.value.substring(pcnt,n);
        }
    }

    if(ccnt) RemoveComma(obj);

    n=obj.value.length;
    if(flg==1 && n==0) {
        ErrMsg(obj , EMPTY , name);
        return(FALSE);
    }

    if(ch=="-") {
        for( i=0 ; i<obj.value.length ; i++ )
        {
            if(( obj.value.charAt(i) >= "0" && obj.value.charAt(i) <= "9" ) || obj.value.charAt(i) == "." || obj.value.charAt(i) == "-" ){
                continue;
            } else {
                ErrMsg(obj , ACCEPT , name + "を半角数字(-可)");
                return( FALSE);
            }
        }
    } else {
        for(i=0;i<obj.value.length ;i++) {
            if(( obj.value.charAt(i) >= "0" && obj.value.charAt(i) <= "9" )) {
                continue;
            } else {
                ErrMsg(obj , ACCEPT , name + "を半角数字");
                return(FALSE);
            }
        }
    }

    return(TRUE);
}

function CalcCharCnt(obj,ch,pzero)
{
    var cnt=0;
    var n,i;

    n=obj.value.length;

    if(pzero==1) {
        for(i=0;i<n;i++) {
            if(obj.value.charAt(i)!="0") return cnt;
            else cnt++;
        }
    } else if(pzero=="-") {
        for(i=1;i<n;i++) {
            if(obj.value.charAt(i)!="0") {
                if(obj.value.charAt(i)==".")return cnt-1;
                else return cnt;
            }else cnt++;
        }
    } else {
        for(i=0;i<n;i++) {
            if(obj.value.charAt(i)==ch) cnt++;
        }
        return(cnt);
    }

    return(cnt);
}

function  spacetrim(mode,obj)
{
    var i,lcnt,rcnt,n;

    lcnt=0;
    rcnt=0;
    n=obj.value.length;

    for(i=0;i<n;i++) {
        if(obj.value.charAt(i)==" " || obj.value.charAt(i)=="　") lcnt++;
        else break;
    }

    if(lcnt) {
        obj.value=obj.value.substring(lcnt,n);
        n=obj.value.length;
    }

    for(i=n-1;i>=0;i--) {
        if(obj.value.charAt(i)==" " || obj.value.charAt(i)=="　") rcnt++;
        else break;
    }

    if(rcnt) obj.value=obj.value.substring(0,n-rcnt);
}

function  spacetrim2(mode,obj)
{
    var i,lcnt,rcnt,n;

    lcnt=0;
    rcnt=0;
    n=obj.length;

    for(i=0;i<n;i++) {
        if(obj.charAt(i)==" " || obj.charAt(i)=="　") lcnt++;
        else break;
    }

    if(lcnt) {
        obj=obj.substring(lcnt,n);
        n=obj.length;
    }

    for(i=n-1;i>=0;i--) {
        if(obj.charAt(i)==" " || obj.charAt(i)=="　") rcnt++;
        else break;
    }

    if(rcnt) obj=obj.substring(0,n-rcnt);

    return(obj);
}

function RemoveComma(obj) {
    var i,n,nval;

    n=obj.value.length;

    if(n==0) return;

    nval="";

    for(i=0;i<n;i++) {
        var nchar =obj.value.charAt(i);
        if( nchar !=",") nval=nval+nchar;
    }

    obj.value=nval;
}

function IsFloat(flg,obj,name,ch)
{
    var	zcnt,dcnt,ccnt,mcnt,n,pcnt,i;

    zcnt=0;dcnt=0;ccnt=0;mcnt=0;
    spacetrim(0,obj);

    n=obj.value.length;

    if(flg && n==0) {
        ErrMsg(obj , EMPTY , name);
        return(FALSE);
    }

    zcnt=CalcCharCnt(obj,"0",0);
    dcnt=CalcCharCnt(obj,".",0);
    ccnt=CalcCharCnt(obj,",",0);
    mcnt=CalcCharCnt(obj,"-",0);

    if(dcnt>1) {
        ErrMsg(obj , ACCEPT , name + "を半角数字（小数点可）");
        return FALSE;
    }

    if(zcnt==n && n != 0) {
        obj.value="0";
        return TRUE;
    }

    if(mcnt) {
        if(mcnt>1) {
            ErrMsg(obj , ACCEPT , name + "を半角数字（小数点可）");
            return FALSE;
        }

        if(obj.value.charAt(0) != "-") {
            ErrMsg(obj , ACCEPT , name + "を半角数字（小数点可）");
            return FALSE;
        }
    }

    if(mcnt) pcnt=CalcCharCnt(obj,"0","-");
    else pcnt=CalcCharCnt(obj,"0",1);

    if(pcnt>0) {
        if(mcnt) {
            if(pcnt== obj.value.length-1) obj.value="-0";
            else if(obj.value.charAt(2) != ".") obj.value="-"+obj.value.substring(pcnt+1,n);
        }else {
            if(obj.value.charAt(1) != ".") obj.value=obj.value.substring(pcnt,n);
        }
    }

    if(ccnt) RemoveComma(obj);

    n=obj.value.length;
    if(flg==1 && n==0) {
        ErrMsg(obj , EMPTY , name);
        return(FALSE);
    }

    if(ch=="-") {
        for( i=0 ; i<obj.value.length ; i++ )
        {
            if(( obj.value.charAt(i) >= "0" && obj.value.charAt(i) <= "9" ) || obj.value.charAt(i) == "." || obj.value.charAt(i) == "-" ){
                continue;
            } else {
                ErrMsg(obj , ACCEPT , name + "を半角数字（-可,小数点）のみ");
                return( FALSE);
            }
        }
    }
    else {
        for(i=0;i<obj.value.length ;i++) {
            if(( obj.value.charAt(i) >= "0" && obj.value.charAt(i) <= "9") || (obj.value.charAt(i) == ".") ) {
                continue;
            } else {
                ErrMsg(obj , ACCEPT , name + "を半角数字（小数点可）のみ");
                return(FALSE);
            }
        }
    }

    if(mcnt>0) {
        if(obj.value.charAt(1)==".") obj.value= "-0."+obj.value.substring(2,n);
        else if(obj.value.charAt(0)==".") obj.value= "0."+obj.value.substring(1,n);
    } else {
        if(obj.value.charAt(0)==".") obj.value= "0."+obj.value.substring(1,n);
    }

    return(TRUE);
}

function IsNumAlpha( flg, obj , name )
{
    var i;
    spacetrim(0,obj);

    if(flg && obj.value.length==0) {
        ErrMsg(obj , ACCEPT , name + "を半角英数字");
        return( FALSE );
    }

    var	wRet;
    wRet = IsEmptySub( 0, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    for( i=0 ; i<obj.value.length ; i++ )
    {
        if(( obj.value.charAt(i) >= "A" && obj.value.charAt(i) <= "Z" ) ||
            ( obj.value.charAt(i) >= "a" && obj.value.charAt(i) <= "z" )  ||
            ( obj.value.charAt(i) >= "0" && obj.value.charAt(i) <= "9" ) || obj.value.charAt(i)==" ")		{
            continue;
        }else{
            ErrMsg(obj , ACCEPT , name + "を半角英数字");
            return( FALSE );
        }
    }

    return(TRUE);
}

function IsNumKigoAlpha( flg, obj , name )
{
    var numalphaStr = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&'(),.<>?/*+-\:;[]{}^|=~ @`_";
    var thisChar;
    var i = 0;

    spacetrim(0,obj);

    if(flg && obj.value.length==0) {
        ErrMsg(obj , ACCEPT , name + "を半角英数字");
        return( FALSE );
    }

    var	wRet;
    wRet = IsEmptySub( 0, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    var cnt = 0;
    for(i=0;i<obj.value.length;i++)
    {
        thisChar = obj.value.substring(i,i+1);
        if (thisChar=='"' || numalphaStr.indexOf(thisChar) != -1)  cnt++;
    }

    if (cnt == 0) {
        ErrMsg(obj , ACCEPT , name + "を半角英数字記号");
        return(FALSE);
    } else {
        if (cnt == obj.value.length) {
            return(TRUE);
        } else {
            ErrMsg(obj , ACCEPT , name + "を半角英数字記号");
            return(FALSE);
        }
    }
}

function IsNumKigoAlpha2( flg, obj , name )
{
    var numalphaStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789#$%*+-?~,./:;=()![]{}@^_|";
    var thisChar;
    var i = 0;

    if (obj.value.length == 0) {
        if (flg) {
            ErrMsg(obj , ACCEPT , name + "を半角英数字");
            return( FALSE );
        } else {
            return( TRUE );
        }
    }

    var cnt = 0;
    for(i=0;i<obj.value.length;i++)
    {
        thisChar = obj.value.substring(i,i+1);
        if (numalphaStr.indexOf(thisChar) != -1)  cnt++;
    }

    if (cnt == 0) {
        ErrMsg(obj , ACCEPT , name + "を半角英数字");
        return(FALSE);
    } else {
        if (cnt == obj.value.length) {
            return(TRUE);
        } else {
            ErrMsg(obj , ACCEPT , name + "を半角英数字");
            return(FALSE);
        }
    }
}

function IsDoubleNumKana( flg, obj , name )
{
    var MojiTbl = "　０１２３４５６７８９アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン" +
        "ガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポァィゥェォヵヶッャュョヮヴヰヱー";

    var i;
    var	wRet;
    var thisChar;

    spacetrim(0,obj);

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    for( i=0 ; i<obj.value.length ; i++ )
    {
        thisChar = obj.value.substring(i,i+1);
        if (MojiTbl.indexOf(thisChar) == -1) {
            ErrMsg(obj , ACCEPT , name + "を全角カタカナと数字");
            return( FALSE );
        }
    }

    return( TRUE );
}

function IsDoubleNumKanaAlpha( flg, obj , name )
{
    var MojiTbl = "　０１２３４５６７８９ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ" +
        "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン" +
        "ガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポァィゥェォヵヶッャュョヮヴヰヱー";
    var i;
    var	wRet;
    var thisChar;

    spacetrim(0,obj);

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    for(i=0;i<obj.value.length;i++ ) {
        thisChar = obj.value.charAt(i);
        if (MojiTbl.indexOf(thisChar) == -1) {
            ErrMsg(obj , ACCEPT , name + "を全角カタカナと英数字");
            return( FALSE );
        }
    }

    return( TRUE );
}

function IsDoubleNumKanaHiraAlpha( flg, obj , name )
{
    var MojiTbl = "　０１２３４５６７８９ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ" +
        "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン" +
        "ガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポァィゥェォヵヶッャュョヮヴヰヱー" +
        "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんゐゑ" +
        "がぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽぁぃぅぇぉっゃゅょゎ";
    var i;
    var	wRet,thisChar;

    spacetrim(0,obj);

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    for( i=0 ; i<obj.value.length ; i++ )
    {
        thisChar = obj.value.substring(i,i+1);
        if (MojiTbl.indexOf(thisChar) == -1) {
            ErrMsg(obj , ACCEPT , name + "を全角カタカナ、ひらがなと英数字");
            return( FALSE );
        }
    }
    return( TRUE );
}

function IsMailAddress( flg, obj , name )
{
    var i;
    var wRet;
    var thisChar;
    var tblM = "\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    spacetrim(0,obj);

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    if (obj.value.indexOf('@') != -1) {
        for( i=0; i<obj.value.length; i++ ) {
            thisChar = obj.value.substring(i,i+1);
            if( tblM.indexOf(thisChar) == -1 ) {
                ErrMsg(obj , WRONG , name );
                return(FALSE);
            }
        }
        return(TRUE);
    } else {
        ErrMsg(obj , WRONG , name );
        return(FALSE);
    }
}

function IsContainKana(flg,obj,name)
{
    var tblK ="ｦｧｨｩｪｫｬｭｮｯｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝ｡｢｣､･ｰﾞﾟ";
    var	wRet;

    spacetrim(0,obj);

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    wRet=IsContainKanaSub(obj, tblK);

    if(wRet==TRUE) {
        ErrMsg(obj , ACCEPT , name + "は半角カタカナを使わない");
        return(FALSE);
    }

    return(TRUE);
}

function IsContainKanaKigo(flg,obj,name)
{
    var tblKK="ｦｧｨｩｪｫｬｭｮｯｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝ !\"#$%&'()*+,^./:;<=>?@[\\]^_`{|}~｡｢｣､･ｰ,ﾞﾟ";
    var	wRet;

    spacetrim(0,obj);

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    wRet=IsContainKanaSub(obj, tblKK);

    if(wRet==TRUE) {
        ErrMsg(obj , ACCEPT , name + "は半角カタカナ、半角記号を使わない");
        return(FALSE);
    }

    return(TRUE);
}

function IsFreeText(flg,obj,name)
{
    var tblKKF="ｦｧｨｩｪｫｬｭｮｯｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝ｡｢｣､･ｰ,ﾞﾟ";
    var	wRet;

    if( flg == 0 ){
        if( obj.value.length <= 0 ) return( TRUE );
    }else{
        if( obj.value.length <= 0 ){
            ErrMsg(obj , EMPTY , name);
            return( FALSE );
        }
    }

    wRet=IsContainKanaSub(obj, tblKKF);

    if(wRet==TRUE) {
        ErrMsg(obj , ACCEPT , name + "は半角カタカナおよび半角記号を使わない");
        return(FALSE);
    }

    return(TRUE);
}

function IsContainKanaSub(obj,tbls)
{
    var thisChar;

    for( var i=0 ; i<obj.value.length ; i++ )
    {
        thisChar = obj.value.substring(i,i+1);
        if (tbls.indexOf(thisChar) >=0) return( TRUE );
    }

    return(FALSE);
}

function IsBigAlpha( flg, obj , name )
{
    var	wRet;
    var tbl1 ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    var thisChar;

    spacetrim(0,obj);

    wRet = IsEmptySub( flg, obj , name );
    if( wRet < 0 )
    {
        return( FALSE );
    }else if( wRet == 0 ){
        return( TRUE );
    }

    obj.value = obj.value.toUpperCase();

    for( var i=0 ; i<obj.value.length ; i++ ){
        thisChar = obj.value.substring(i,i+1);
        if (tbl1.indexOf(thisChar) < 0) {
            ErrMsg(obj , ACCEPT , name + "は大文字英字");
            return( FALSE );
        }
    }

    return(TRUE);
}

function getPrivateCookie(key1, key2)
{
    var tmp, tmp1, start, end;
    var KEY;
    var roopcnt;
    var n;

    if( IsNAN(key2) == TRUE ) retrun("");

    KEY = "SBCookies" + ( parseInt(key2,10) * 611 + 35 );
    tmp = document.cookie + ";";
    tmp1 = tmp.indexOf( KEY, 0 );

    if(tmp1 != -1)
    {
        tmp = tmp.substring(tmp1, tmp.length);
        start = tmp.indexOf("=", 0) + 1;
        end = tmp.indexOf(";", start);
        tmp = tmp.substring(start, end);
    }else{
        return("");
    }

    if(key1 == "CookieId")
    {
        roopcnt = 1;
    }else if(key1 == "TanmatsuShubt"){
        roopcnt = 2;
    }else if(key1 == "ZenkaiLogonTime"){
        roopcnt = 3;
    }else{
        return("");
    }

    tmp1 = 0;

    for(n = 1; n <= roopcnt; n++)
    {
        start = parseInt(tmp1,10);
        tmp1 = tmp.indexOf("_",tmp1) + 1;
        if(tmp1 == 0) return("");
        end = parseInt(tmp1,10) - 1;
    }

    return(unescape(tmp.substring(start, end)));
}

function setPrivateCookie( COOKIEID, TANMATSUSHUBT, ZENKAILOGONTIME, COOKIEYUKOKIGEN, UID )
{
    var CookieInformation;
    var pdate = COOKIEYUKOKIGEN;
    var gmtdate = DateToGMTdate(pdate);

    CookieInformation = COOKIEID + "_" + TANMATSUSHUBT + "_" + ZENKAILOGONTIME + "_";
    document.cookie = "SBCookies" + (parseInt(UID,10) * 611 + 35) + "=" + CookieInformation + "; path=/" + "; expires= " + gmtdate;
}

function DateToGMTdate(pdate)
{
    var edate = new Date();
    var gmtdate;

    edate.setYear(parseInt(pdate.substring(0, 4),10));
    edate.setMonth(parseInt(pdate.substring(5, 7),10) - 1);
    edate.setDate(parseInt(pdate.substring(8, 10),10));
    edate.setHours(23);
    edate.setMinutes(59);
    edate.setSeconds(59);

    gmtdate = edate.toGMTString();

    return(gmtdate);
}

function InputKetaChk( obj, name, keta )
{
    if(obj.value.length > keta)
    {
        ErrMsg(obj , WRONG , name );
        return( FALSE );
    }

    return( TRUE );
}
