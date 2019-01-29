<script src="jquery-2.2.3.min.js" type="text/javascript"></script>
<button>begin</button>
<script>
    //redPackageId  userId

    $(function () {
        $("button").click(function () {
            var j = 0;
            var i;
            var start = new Date();
            for( i = 1; i <= 20000 ; i ++){
                $.ajax({
                    url:"http://localhost:8080/grabRedPackageByRedis?redPackageId=1&userId=" +i,
                    async:false,
                    success:function(data) {
                        if(data === 'ok'){
                            j++;
                        }
                        // console.log(data);
                    }
                })
            }
            console.log("j = " + j);
            console.log("i = " + i);
            console.log("共耗时 = " + (new Date() - start));
        })
    });
</script>
