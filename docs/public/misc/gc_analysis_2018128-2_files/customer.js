/*$(window).on('beforeunload', function() {
	 $(window).scrollTop(0);
});*/
$(document).ready(function() {

    	$("a.cd-nav-trigger").on( "click", function() {
               setTimeout(function(){
                   var checkcls =$( "a.cd-nav-trigger" ).hasClass( "close-nav" );
                    if (checkcls) {
                       $('#ascrail2000').css('display','none');
                    }
                    else{
                         $('#ascrail2000').css('display','block');
                    }
               }, 500);
               
          });
    
	var checkpgebanner = $("body div.main-content").hasClass('nobannerpage');
	if (checkpgebanner) {
		$("body header div.header div.top-sec").addClass('ban-less');
	}

	var find_homepage = $('body').hasClass('home');

	if(find_homepage){
		$('.side-nav a.cd-nav-trigger').addClass('bounceinright');
		$('.side-nav').css('display', 'block');
	}
	
	if(!find_homepage && !localStorage.getItem("countrycode")){
		
		$.get("http://ipinfo.io", function (response) {			
			displayTollNo(response.country);	
			localStorage.setItem("countrycode", response.country);				
		}, "jsonp");
		
	}
	else if(!find_homepage && localStorage.getItem("countrycode")){
		
		var country_code = localStorage.getItem("countrycode");		
		displayTollNo(country_code);
	}

	function displayTollNo(mycountrycde){
		
		if(mycountrycde == 'GB'){
			$("#toll_free").css("display","none");
			$("#uk_tollfree").css("display","block");	
			$("#email_general").css("display","none");
			$("#uk_email").css("display","block");
		}
		else{
			$("#uk_tollfree").css("display","none");
			$("#toll_free").css("display","block");	
			$("#email_general").css("display","block");
			$("#uk_email").css("display","none");
		}			
	}
	
	
	
	/*
	* GetInTouch Form Scripts
	*/
	$(".frm-overlay").on( "click", function() {
		$(".active-form").removeClass("active-form");
		$(this).css("display","none");
		$('#getintouchForm .wpcf7-form p.form-submit input.wpcf7-submit').prop('value', 'SEND');
		$('#getintouchForm .wpcf7-form').find('.form-submit').removeClass('suces');
		$('body').css('overflow', '');
		setTimeout(function() {
			$('#getintouchForm.thm-slide-form').removeAttr( 'style' );
		},500);
		if ($(window).scrollTop() > 200){
			$('.side-nav').show();
			$('.top-icon').show();
		}
        $('#ascrail2000').css('display','block');
	});
	$('.get_in_touch').on( "click", function() {
		$('.side-nav').hide();
		$('.top-icon').hide();
		//$('#getintouchForm').insertAfter($('#page'));
		$('#page').addClass('active-form');
		$('body').css('overflow', 'hidden');
		$('.partners-frm-overlay').css('display','none');
		$('#partnersForm.thm-slide-form').css('display','none');		
		var test_for_visualizer = $(this).hasClass('product-visualizer');		
		if(test_for_visualizer){
			$('#getintouchForm h3.main-title').text("Request Free Demo");
		}
		$('.frm-overlay').css('display','block');
		$('#getintouchForm.thm-slide-form').css('display','block');
        $('#ascrail2000').css('display','none');
		
	});
	$("#getintouchForm span.frm-close").on( "click", function() {
		$('#page').removeClass('active-form');
		$('.frm-overlay').css('display','none');
		$('body').css('overflow', '');
		//$('#getintouchForm .wpcf7-form')[0].reset();	
		$('.wpcf7-form').trigger("reset");
		$('#getintouchForm .wpcf7-form p.form-submit input.wpcf7-submit').prop('value', 'SEND');
		$('#getintouchForm .wpcf7-form').find('.valid-error').removeClass('valid-error');		
		$('#getintouchForm .wpcf7-form').find('.wpcf7-response-output').text('');
		$('#getintouchForm .wpcf7-form').find('.form-submit').removeClass('suces');
		$('#getintouchForm p.form-submit input.wpcf7-submit').prop('value', 'SEND');
		setTimeout(function() {
			$('#getintouchForm.thm-slide-form').removeAttr( 'style' );
			$('#getintouchForm p.form-submit input.wpcf7-submit').prop('value', 'SEND');
		},500);
		if ($(window).scrollTop() > 200){
			$('.side-nav').show();
			$('.top-icon').show();
		}
        $('#ascrail2000').css('display','block');
	});

	/*
	* Partners & Consulting Form Scripts
	*/
	$(".partners-frm-overlay").on( "click", function() {
		$(".active-form").removeClass("active-form");
		$(this).css("display","none");
		$('#partnersForm .wpcf7 p.form-submit input.wpcf7-submit').prop('value', 'SEND');
		$('#partnersForm .wpcf7-form').find('.form-submit').removeClass('suces');
		$('body').css('overflow', '');
		setTimeout(function() {
			$('#partnersForm.thm-slide-form').removeAttr( 'style' );
			$('#partnersForm .wpcf7 p.form-submit input.wpcf7-submit').prop('value', 'SEND');
		},500);
		if ($(window).scrollTop() > 200){
			$('.side-nav').show();
			$('.top-icon').show();
		}
        $('#ascrail2000').css('display','block');
	});
	$('.partners_form').on( "click", function() {
		$('.side-nav').hide();
		$('.top-icon').hide();
		$('#partnersForm').insertAfter($('#page'));
		$('#partnersForm.thm-slide-form').css('display','block');
		$('#page').addClass('active-form');
		$('body').css('overflow', 'hidden');
		var test_for_consulting = $(this).hasClass('consultingPageBns');		
		if(test_for_consulting){
			$('#partnersForm h3.main-title').text("Drop us a Message");
			$('#partnersForm textarea.frm-txtarea').text("What's on your mind?");
			$('#partnersForm .wpcf7 p.form-submit input.wpcf7-submit').prop('value', 'SEND');
		}
		$('.partners-frm-overlay').css('display','block');
		$('#ascrail2000').css('display','none');
	});
	$("#partnersForm span.frm-close").on( "click", function() {		
		$('#page').removeClass('active-form');
		$('body').css('overflow', '');
		//$('#partnersForm .wpcf7-form')[1].reset();	
		$('#partnersForm .wpcf7-form').trigger("reset");	
		$('#partnersForm .wpcf7 p.form-submit input.wpcf7-submit').prop('value', 'SEND');
		$('#partnersForm .wpcf7-form').find('.valid-error').removeClass('valid-error');		
		$('#partnersForm .wpcf7-form').find('.wpcf7-response-output').text('');
		$('#partnersForm .wpcf7-form').find('.form-submit').removeClass('suces');
		setTimeout(function() {
			$('#partnersForm.thm-slide-form').removeAttr( 'style' );
			$('#partnersForm .wpcf7 p.form-submit input.wpcf7-submit').prop('value', 'SEND');
		},500);
		if ($(window).scrollTop() > 200){
			$('.side-nav').show();
			$('.top-icon').show();
		}
        $('#ascrail2000').css('display','block');
	});
	
	/*Menu Script*/
	$('.cd-nav-trigger').on("click", function(){
		if($('.cd-nav-trigger').hasClass('close-nav')){
			setTimeout(function() { 
    			$('body').css('overflow','');
			}, 500);
		}
		else{
			$('body').css('overflow','hidden');
		}
	});

	
	if (!find_homepage) {
		$('div.header').removeClass('fit_window');
		$('.top-info').css('display', 'block');
	};
	var find_single_job_page = $('body').hasClass('single-job_listing');
	if (find_single_job_page) {
		$('header').remove('.entry-header');
	}
	$('.site_single_job_content .job_listing div').removeClass('entry-content');
	$('.fit_window').css({ height: $(window).innerHeight() });
	$(window).resize(function(e){
		$('.fit_window').css({ height: $(window).innerHeight() });
	});

	$("#right-menu li:last-child").addClass("lets-talk");

	
	//Team Description JS
	
	
	$('.shrt-desc').click(function(){
		if(!$(this).next('.full-desc').hasClass('desc-show') && !$(this).parent('li').hasClass('desc-osp') && !$(this).find('.nmnrol').hasClass('active')){
			$('.full-desc').removeClass('desc-show');
			$('.team-list li').removeClass('desc-osp');
			$('.nmnrol').removeClass('active');
		}
		$(this).parent('li').toggleClass('desc-osp');
		$(this).next('.full-desc').toggleClass('desc-show');
		$(this).find('.nmnrol').toggleClass('active');
	
	
		$('.side-nav .cd-nav-trigger').click(function(){
			$('body').toggleClass('mob-menu-bh');
		});
		
		
		/* $('.about-sec .team-list li').css({'margin-bottom':'10px'});
		
		var team_list_width = $(this).next('.full-desc').innerHeight();
		
		$('.about-sec .team-list li.desc-osp').css({'margin-bottom':team_list_width});
		
		$(window).resize(function(e){
			var team_list_width = $(this).next('.full-desc').innerHeight();
			$('.about-sec .team-list li.desc-osp').css({'margin-bottom':team_list_width});
		}); */
	
	});
	/*
	*Highlight Menu
	*/
	var url = window.location.href;	

	// Will only work if string in href matches with location
	$('.menu-sec a[href="'+ url +'"]').addClass('active');

	// Will also work for relative and absolute hrefs
	$('.menu-sec a').filter(function() {
	    return this.href == url;
	}).addClass('relative-active');
	
	
	
	/*Landing Page fit screen condition*/
	
	var win_height = $(window).innerHeight();
	var win_width = $(window).innerWidth();
	var heightland = win_height;
	var widthland = win_width / 3;
	var heightland_res = win_height / 3;
	var widthland_res = win_width;
	
	if (win_width > win_height){
		$('.landing-sec').removeClass('potr_scren');
		$('.fit_window_land').css({ 'height': heightland,'width': widthland});
		$('.fit_window_land:nth-child(2)').css({ 'left': widthland });
		$('.fit_window_land:nth-child(3)').css({ 'left': widthland*2 });
	}
	else{
		$('.landing-sec').addClass('potr_scren');
		$('.fit_window_land').css({ 'height': heightland_res, 'width': widthland_res, 'border-right': 'none'/*, 'border-bottom': 'solid 1px #fff'*/ });
		$('.fit_window_land:nth-child(2)').css({ 'top': heightland_res });
		$('.fit_window_land:nth-child(3)').css({ 'top': heightland_res*2 });
	}
	
	$(window).resize(function(e){
	
		var win_height = $(window).innerHeight();
		var win_width = $(window).innerWidth();
		var heightland = win_height;
		var widthland = win_width / 3;
		var heightland_res = win_height / 3;
		var widthland_res = win_width;
		
		
		if (win_width > win_height){
			$('.landing-sec').removeClass('potr_scren');
			$('.fit_window_land').css({ 'height': heightland, 'width': widthland});
			if(!$('.fit_window_land').hasClass('land_width_full')){
				$('.fit_window_land:nth-child(2)').css({ 'top':'','left': widthland });
				$('.fit_window_land:nth-child(3)').css({ 'top':'','left': widthland*2 });
			}
		}
		else{
			$('.landing-sec').addClass('potr_scren');
			$('.fit_window_land').css({ 'height': heightland_res, 'width': widthland_res, 'border-right': 'none'/*,'border-bottom': 'solid 1px #fff'*/ });
			if(!$('.fit_window_land').hasClass('land_width_full')){
				$('.fit_window_land:nth-child(2)').css({ 'left':'','top': heightland_res });
				$('.fit_window_land:nth-child(3)').css({ 'left':'','top': heightland_res*2 });
			}
		}
	});
	
	
	var minus_width = -win_width / 3;
	var minus_height = -win_height / 3;
	
	$('.circle-sec').click(function(){
		
		var cur_click = $(this);
		$(cur_click).next('.cnt-circle').addClass('open');
		$('.cd-nav-trigger').hide();
		$(cur_click).parent('.fit_window_land').css({'z-index':'1007'});
		$(cur_click).next('.cnt-circle').delay(400).hide(200);
		$('.home-top-sec').hide(200);
		$(cur_click).delay(400).hide(200);
		$(cur_click).siblings('.sec-cntnt').delay(400).fadeIn(100);
		$("html, body").animate({ scrollTop: 0 }, 200);
		
		
		if (win_width > win_height){
			
			$(cur_click).parent('.inner-sec').addClass('land_width_full');
			if($(cur_click).parent().hasClass('commerce')){
				$('.fit_window_land:nth-child(2),.fit_window_land:nth-child(3)').css({ 'top':'','left': '100%' });
			}
			else if($(cur_click).parent().hasClass('content')){
				$('.content').css({'left':'0','top':'0'});
				$('.fit_window_land:nth-child(1)').css({ 'top':'','left': minus_width });
				$('.fit_window_land:nth-child(3)').css({ 'top':'','left': '100%' });
			}
			else if($(cur_click).parent().hasClass('consulting')){
				$('.consulting').css({'left':'0','top':'0'});
				$('.fit_window_land:nth-child(1)').css({ 'top':'','left': minus_width });
				$('.fit_window_land:nth-child(2)').css({ 'top':'','left': minus_width*2 });
			}
				
			$('.fit_window_land').css({'border-right': '' });
		}
		
		
		
		else{
			$('.landing-sec').removeClass('potr_scren');
			$(cur_click).parent('.inner-sec').addClass('land_width_full');
			if($(cur_click).parent().hasClass('commerce')){
				$('.fit_window_land:nth-child(2),.fit_window_land:nth-child(3)').css({ 'top':'100%','left': '' });
			}
			else if($(cur_click).parent().hasClass('content')){
				$('.content').css({'left':'0','top':'0'});
				$('.fit_window_land:nth-child(1)').css({ 'top': minus_height,'left': '' });
				$('.fit_window_land:nth-child(3)').css({ 'top':'100%','left': '' });
			}
			else if($(cur_click).parent().hasClass('consulting')){
				$('.consulting').css({'left':'0','top':'0'});
				$('.fit_window_land:nth-child(1)').css({ 'top':minus_height ,'left': '' });
				$('.fit_window_land:nth-child(2)').css({ 'top':minus_height*2 ,'left': '' });
			}
			$('.fit_window_land').css({ 'border': '' });
			
		}
		
		
		
		
		
		$('html').css({'overflow':'hidden'});
		
	});
	
	
	
	$('.land-close').click(function(){
		//$('body').delay(500).queue(function(){
			$('html').css({'overflow':''});
			$('.cd-nav-trigger').show();
			$('.sec-cntnt').hide(200);
			$('.inner-sec').removeClass('land_width_full');
			$('.cnt-circle').removeClass('open').show();
			$('.fit_window_land').css({'z-index':''});
			$('.home-top-sec').show(200);
			$('.circle-sec').delay(400).show(200);
		//});
		
		
			if (win_width > win_height){
			
				$('.fit_window_land').css({ 'height': heightland, 'width': widthland});
				if(!$('.fit_window_land').hasClass('land_width_full')){
					$('.fit_window_land:nth-child(1)').css({ 'top':'','left': '' });
					$('.fit_window_land:nth-child(2)').css({ 'top':'','left': widthland });
					$('.fit_window_land:nth-child(3)').css({ 'top':'','left': widthland*2 });
				}
			}
			else{
				$('.landing-sec').addClass('potr_scren');
				$('.fit_window_land').css({ 'height': heightland_res, 'width': widthland_res, 'border-right': 'none'/*,'border-bottom': 'solid 1px #fff' */});
				if(!$('.fit_window_land').hasClass('land_width_full')){
					$('.fit_window_land:nth-child(1)').css({ 'left':'','top': '' });
					$('.fit_window_land:nth-child(2)').css({ 'left':'','top': heightland_res });
					$('.fit_window_land:nth-child(3)').css({ 'left':'','top': heightland_res*2 });
				}
			}

	});
	
	$(window).resize(function(e){

		var win_height = $(window).innerHeight();
		var win_width = $(window).innerWidth();
		var heightland = win_height;
		var widthland = win_width / 3;
		var heightland_res = win_height / 3;
		var widthland_res = win_width;
		
			//$('body').delay(500).queue(function(){
				$('html').css({'overflow':''});
				$('.cd-nav-trigger').show();
				$('.sec-cntnt').hide(200);
				$('.inner-sec').removeClass('land_width_full');
				$('.cnt-circle').removeClass('open').show();
				$('.fit_window_land').css({'z-index':''});
				$('.home-top-sec').show(200);
				$('.circle-sec').delay(400).show(200);
			//});
			
			
				if (win_width > win_height){
				
					$('.fit_window_land').css({ 'height': heightland, 'width': widthland});
					if(!$('.fit_window_land').hasClass('land_width_full')){
						$('.fit_window_land:nth-child(1)').css({ 'top':'','left': '' });
						$('.fit_window_land:nth-child(2)').css({ 'top':'','left': widthland });
						$('.fit_window_land:nth-child(3)').css({ 'top':'','left': widthland*2 });
					}
				}
				else{
					
					$('.fit_window_land').css({ 'height': heightland_res, 'width': widthland_res, 'border-right': 'none'/*,'border-bottom': 'solid 1px #fff' */});
					if(!$('.fit_window_land').hasClass('land_width_full')){
						$('.fit_window_land:nth-child(1)').css({ 'left':'','top': '' });
						$('.fit_window_land:nth-child(2)').css({ 'left':'','top': heightland_res });
						$('.fit_window_land:nth-child(3)').css({ 'left':'','top': heightland_res*2 });
					}
				}

		
		var win_height_resz = $(window).innerHeight();
		$('.land-down').click(function(){
			$("html, body").animate({ scrollTop: win_height_resz }, 200);
		});
	});
	
	$('.land-down').click(function(){
		$("html, body").animate({ scrollTop: win_height }, 200);
	});
	
	
	
	/*End Landing Page fit screen condition*/
	
	/*Careers Inner Page*/
	var jobfrm = $('#job-apply').innerHeight();
	$('.site_single_job_content .cntnt-row.commerce-sec').css('min-height',jobfrm + 250);
	
	
	
	$('.top-icon').click(function(){
		//$(window).scrollTop(0);
		$('html, body').animate(
			{ scrollTop: 0 },
			{
				duration: 500,
				easing: 'linear'
				// try using 'swing' too
				// 'easeInOutExpo' is supported with jQuery UI
			
		});
        return false;
	});
	
	/*Brands Show more*/
	$('#brandshow').click(function(){
		if($(this).hasClass("less")) {
	        $(this).removeClass("less");
	        $(this).removeClass("shw-less");
	        $(this).text("Show More");
	        $('.logos-list').removeClass('shw-more');

	    } else {
	        $(this).addClass("less");
	        $(this).addClass('shw-less');
	        $(this).text("Show Less");
	        $('.logos-list').addClass('shw-more');	          
	    }
	    //$(this).parent().prev().toggle();
	    //$(this).prev().toggle();
	    return false;		
	});
	
	/*Brands Show more*/
	/*$('#brandshow').click(function(){
		$(this).hide();		
		$('.logos-list').addClass('shw-more');
	});*/

	/*Scroll to top*/
	$(window).bind('scroll', function () {
		if ($(window).scrollTop() > 200) {
			$('.top-icon').show();
		}
		else{
			$('.top-icon').hide();
		}
	});
	/*End Scroll to top*/
	
	$(window).bind('scroll', function () {
		
		if(!find_homepage){
			if ($(window).scrollTop() > 200) {
			  $('.side-nav a.cd-nav-trigger').addClass('bounceinright');
			  $('.side-nav').css('display', 'block');
		 	} else {
			  $('.side-nav').removeAttr( 'style' );
			  $('.side-nav a.cd-nav-trigger').removeClass('bounceinright, hidden, visible, animated, bounceInRight');
		 	}
		}
	
		 
	});	
	

	/*if($(window).width() < 1048){
		
		var work_url = window.location.href;
		var check_pgeslug = "our-work.htm";
		if(work_url.indexOf(check_pgeslug) != -1){
    		
    		$("#myCarousel .video-play iframe").each(function( index ) {  				
  				var videoiframeurl = $( this ).attr('src');
  				var appendquerystr = videoiframeurl+"&autoplay=1";    		
    			$( this ).attr('src',appendquerystr);
    			
			});
    		
		}	
	}*/

		
	if ($(window).width() > 1048) {

	$('.fadein').addClass("hidden").viewportChecker({
		classToAdd: 'visible animated fadeIn', // Class to add to the elements when they are visible
		offset: 100    
	}); 
	$('.bouceindown').addClass("hidden").viewportChecker({
		classToAdd: 'visible animated bounceInDown', // Class to add to the elements when they are visible
		offset: 100    
	});   
	$('.bouceinup').addClass("hidden").viewportChecker({
		classToAdd: 'visible animated bounceInUp', // Class to add to the elements when they are visible
		offset: 100    
	});   
	$('.fadeindown').addClass("hidden").viewportChecker({
		classToAdd: 'visible animated fadeInDown', // Class to add to the elements when they are visible
		offset: 100    
	});
	$('.fadeinup').addClass("hidden").viewportChecker({
		classToAdd: 'visible animated fadeInUp', // Class to add to the elements when they are visible
		offset: 100    
	});
	$('.bounceinleft').addClass("hidden").viewportChecker({
		classToAdd: 'visible animated bounceInLeft', // Class to add to the elements when they are visible
		offset: 100    
	});
	$('.bounceinright').addClass("hidden").viewportChecker({
		classToAdd: 'visible animated bounceInRight', // Class to add to the elements when they are visible
		offset: 100    
	});
	$('.flipinx').addClass("hidden").viewportChecker({
		classToAdd: 'visible animated flipInX', // Class to add to the elements when they are visible
		offset: 100    
	});

	}	
	
	   	$("#year_filter li").on( "click", function() {
			  var li_val = $(this).text();
			  $("#year_filter li").removeClass("active");
			   $(this).addClass("active");
			   var homeURL = $("header .header .menu-sec .logo a").attr('href');
			   //console.log(homeURL);
			   
			 var request = $.ajax({
							  url : homeURL+'/wp-admin/admin-ajax.php',
							  method: "POST",
							  data : {action: "newsroom_sort_by_year",newsYear:li_val}
								  
							});
								 
							request.done(function( msg ) {								
								  
								   $('.newsroom .container-max  .cont').html(msg);
							});								 
							request.fail(function( jqXHR, textStatus ) {
							  alert( "Request failed: " + textStatus );
							}); 
			  
	   });
	

	$('.dropdown').hide();
	$('.archives').click(function(){
		$('.dropdown').slideToggle(50);
		
	});
	$('.archives ul#year_filter li').click(function(){
		var year = $(this).html();
		$('.archives > span').html('(' + year + ')');
	});
	
	
	/*title-stry*/
	$('.succ-stories-item > .title-stry').each(function() {
		var titwidth = $(this).outerWidth();
		$(this).css({'min-width' : titwidth});
	});
	
	/*Mobile Menu ADD ATTR*/
	$('#mob-right-menu a[title="extensions"]').attr('target', '_blank');
	
});


$(window).bind("load", function() { 

	//windowHeight = e.currentTarget.innerHeight;
	//alert(windowHeight);
    // insert your code here 
	$('.preloader').fadeOut(1000);
	
	/*Home page logo animation*/
	var template_dir = $('#temp_dir').val();
	$('.logo-animate img').delay(1000).show().attr('src', template_dir + '/images/home-logo-ani.gif');
	$('.ani-postload').delay(6500).fadeOut(500);
	$('.logo-animate').delay(6500).queue(function (next) { 
		$(this).css('top', '-20px');
		next();
	});
		/*End Home page logo animation*/
});