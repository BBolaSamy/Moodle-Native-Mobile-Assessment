require 'xcodeproj'
require 'pathname'

root = File.expand_path(__dir__)
app_name = 'MoodleAssessmentApp'
project_path = File.join(root, "#{app_name}.xcodeproj")
project = Xcodeproj::Project.new(project_path)

app_target = project.new_target(:application, app_name, :ios, '17.0')
test_target = project.new_target(:unit_test_bundle, "#{app_name}Tests", :ios, '17.0')
test_target.add_dependency(app_target)

app_target.build_configurations.each do |config|
  config.build_settings['PRODUCT_BUNDLE_IDENTIFIER'] = 'com.itcorner.moodleassessmentapp'
  config.build_settings['GENERATE_INFOPLIST_FILE'] = 'YES'
  config.build_settings['INFOPLIST_KEY_UIApplicationSceneManifest_Generation'] = 'YES'
  config.build_settings['INFOPLIST_KEY_UILaunchScreen_Generation'] = 'YES'
  config.build_settings['INFOPLIST_KEY_UIStatusBarStyle'] = 'UIStatusBarStyleDefault'
  config.build_settings['INFOPLIST_KEY_UISupportedInterfaceOrientations_iPhone'] = 'UIInterfaceOrientationPortrait UIInterfaceOrientationLandscapeLeft UIInterfaceOrientationLandscapeRight'
  config.build_settings['INFOPLIST_KEY_UISupportedInterfaceOrientations_iPad'] = 'UIInterfaceOrientationPortrait UIInterfaceOrientationPortraitUpsideDown UIInterfaceOrientationLandscapeLeft UIInterfaceOrientationLandscapeRight'
  config.build_settings['ASSETCATALOG_COMPILER_APPICON_NAME'] = ''
  config.build_settings['ASSETCATALOG_COMPILER_GLOBAL_ACCENT_COLOR_NAME'] = 'AccentColor'
  config.build_settings['IPHONEOS_DEPLOYMENT_TARGET'] = '17.0'
  config.build_settings['SWIFT_VERSION'] = '5.0'
  config.build_settings['TARGETED_DEVICE_FAMILY'] = '1,2'
  config.build_settings['ENABLE_PREVIEWS'] = 'YES'
  config.build_settings['CODE_SIGN_STYLE'] = 'Automatic'
  config.build_settings['DEVELOPMENT_TEAM'] = ''
end

test_target.build_configurations.each do |config|
  config.build_settings['PRODUCT_BUNDLE_IDENTIFIER'] = 'com.itcorner.moodleassessmentapp.tests'
  config.build_settings['GENERATE_INFOPLIST_FILE'] = 'YES'
  config.build_settings['IPHONEOS_DEPLOYMENT_TARGET'] = '17.0'
  config.build_settings['SWIFT_VERSION'] = '5.0'
  config.build_settings['TARGETED_DEVICE_FAMILY'] = '1,2'
  config.build_settings['CODE_SIGN_STYLE'] = 'Automatic'
  config.build_settings['TEST_HOST'] = '$(BUILT_PRODUCTS_DIR)/MoodleAssessmentApp.app/MoodleAssessmentApp'
  config.build_settings['BUNDLE_LOADER'] = '$(TEST_HOST)'
  config.build_settings['LD_RUNPATH_SEARCH_PATHS'] = '$(inherited) @executable_path/Frameworks @loader_path/Frameworks'
end

main_group = project.main_group
app_group = main_group.new_group(app_name, app_name)
app_root = File.join(root, app_name)

Dir.glob(File.join(app_root, '**', '*.swift')).sort.each do |swift_file|
  relative = Pathname(swift_file).relative_path_from(Pathname(app_root)).to_s
  ref = app_group.new_file(relative)
  app_target.source_build_phase.add_file_reference(ref, true)
end

assets_ref = app_group.new_file('Resources/Assets.xcassets')
app_target.resources_build_phase.add_file_reference(assets_ref, true)
preview_assets_ref = app_group.new_file('Preview Content/Preview Assets.xcassets')
app_target.resources_build_phase.add_file_reference(preview_assets_ref, true)

tests_group = main_group.new_group("#{app_name}Tests", "#{app_name}Tests")
tests_root = File.join(root, "#{app_name}Tests")
Dir.glob(File.join(tests_root, '**', '*.swift')).sort.each do |swift_file|
  relative = Pathname(swift_file).relative_path_from(Pathname(tests_root)).to_s
  ref = tests_group.new_file(relative)
  test_target.source_build_phase.add_file_reference(ref, true)
end

project.save
puts "Generated #{project_path}"
