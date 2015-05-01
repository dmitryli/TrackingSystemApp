var gulp = require('gulp'),
	gUtil = require('gulp-util'),
	jshint = require('gulp-jshint'),
	uglify = require('gulp-uglify'),
	minifyCss = require('gulp-minify-css'),
	concat = require('gulp-concat'),
	rimraf = require('gulp-rimraf'),
	rename = require('gulp-rename'),
	gsync = require('gulp-sync')(gulp),
	browserSync = require('browser-sync');

// Static server
gulp.task('browser-sync', function () {
	browserSync({
		files: ['./app/views/*.html', './app/js/*.js', './app/data/*.json', './app/css/*.css', './app/*.html'],
		proxy: "localhost:7000"
	});
});

// concat all angular libs and save to dist/js
gulp.task('js-angular', function () {
	return gulp.src(['app/bower_components/jquery/dist/jquery.min.js',
				'app/bower_components/angular/angular.min.js',
				'app/bower_components/angular-loader/angular-loader.min.js',
				'app/bower_components/angular-route/angular-route.min.js',
				'app/bower_components/angular-resource/angular-resource.min.js',
				'app/bower_components/angular-filter/dist/angular-filter.min.js',
				'app/bower_components/angular-animate/angular-animate.min.js',
				'app/bower_components/angular-bootstrap/ui-bootstrap.min.js',
				'app/bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js'
				])
			.pipe(concat('angular.min.js'))
			.pipe(gulp.dest('dist/js'));

	// return gulp.src(['app/bower_components/**/*.min.js', 'app/bower_components/**/*.min.js.map'])
	// 		.pipe(gulp.dest('dist/js'));
});

// lint, uglify and concat into app.js file save to dist/js
gulp.task('js-app', function () {
   return gulp.src('app/js/*.js')
      // .pipe(jshint())
      // .pipe(jshint.reporter('default'))
      .pipe(uglify())
      .pipe(concat('app.min.js'))
      .pipe(gulp.dest('dist/js'));
});

gulp.task('css-bootstrap', function () {
	gulp.src('app/bower_components/bootstrap/dist/fonts/**')
		.pipe(gulp.dest('dist/fonts'));

	return gulp.src(['app/bower_components/bootstrap/dist/css/bootstrap.min.css',
					'app/css/bootstrap-flatly/css/bootstrap.flatly.min.css'])
			// .pipe(concat('bootstrap.css'))
			.pipe(gulp.dest('dist/css'));
});

gulp.task('css-app', function () {
	return gulp.src('app/css/app.css')
		.pipe(minifyCss())
		.pipe(gulp.dest('dist/css'));
});

gulp.task('img', function () {
	return gulp.src('app/images/**')
		.pipe(gulp.dest('dist/images'));
});

gulp.task('html', function () {
	gulp.src('app/index.dist.html')
		.pipe(rename('index.html'))
		.pipe(gulp.dest('dist/'));

	return gulp.src('app/views/**')
		.pipe(gulp.dest('dist/views'));
});

gulp.task('clean', function () {
	return gulp.src(['dist/*.html', 'dist/js', 'dist/css', 'dist/fonts', 'dist/images', 'dist/views'], { read: false })
		.pipe(rimraf());
});

gulp.task('js', ['js-angular', 'js-app']);
gulp.task('css', ['css-bootstrap', 'css-app']);

gulp.task('build', gsync.sync(['clean', 
								['js', 'css', 'img', 'html']]));

gulp.task('default', ['build']);
