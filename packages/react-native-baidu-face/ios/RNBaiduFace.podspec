require "json"
package = JSON.parse(File.read(File.join(__dir__, "..", "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNBaiduFace"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.author       = {"Moi Xiey" => "625233046@qq.com"}
  s.homepage     = "n/a"
  s.license      = "MIT"
  s.platform     = :ios, "10.0"
  s.source       = { :git => "https://github.com/moi-xiey/rn" }

  s.source_files = "**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.dependency "React-Core"
  s.vendored_frameworks = "BDFaceSDK/IDLFaceSDK.framework"
  s.resources = "BDFaceSDK/*.{bundle,face-ios}"
  s.library = "c++", "z"
end
